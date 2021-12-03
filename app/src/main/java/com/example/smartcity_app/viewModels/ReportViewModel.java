package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.R;
import com.example.smartcity_app.mappers.ReportMapper;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.ReportDto;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.ui.fragment.LoginFragment;
import com.example.smartcity_app.ui.fragment.ReportCreationFragment;
import com.example.smartcity_app.utils.InputCheck;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends AndroidViewModel {
    private MutableLiveData<List<Report>> _reports = new MutableLiveData<>();
    private LiveData<List<Report>> reports = _reports;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<HashMap<String , String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private WalloniaFixedWebService webService;
    private ReportMapper reportMapper;

    public ReportViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.reportMapper = ReportMapper.getInstance();
    }

    public void checkData(String description, String street, String houseNumber, String zipCode, String city) {
        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isInputValid(description))
            errors.put("description", getApplication().getResources().getString(R.string.error_description));

        if(!InputCheck.isInputValid(street))
            errors.put("street", getApplication().getResources().getString(R.string.error_street));

        if(!InputCheck.isHouseNumberValid(houseNumber))
            errors.put("houseNumber", getApplication().getResources().getString(R.string.error_house_number));

        if(!InputCheck.isZipCodeValid(zipCode))
            errors.put("zipCode", getApplication().getResources().getString(R.string.error_zip_code));

        if(!InputCheck.isInputValid(city))
            errors.put("city", getApplication().getResources().getString(R.string.error_city));

        _inputErrors.setValue(errors);
    }

    public void getReportsFromWeb() {
        webService.getReports().enqueue(new Callback<List<ReportDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<ReportDto>> call, @NotNull Response<List<ReportDto>> response) {
                if (response.isSuccessful()) {
                    _reports.setValue(reportMapper.mapToReports(response.body()));
                    Log.i("DEBUG", "response.isSuccessful dataCount : " + _reports.getValue().size());
                }else{
                    Log.i("DEBUG", "response.isNotSuccessful");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<ReportDto>> call, @NotNull Throwable t) {
                Log.i("DEBUG", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getReportsFromWebWithUserId(Integer id) {
        webService.getReportsWithUserId(id).enqueue(new Callback<List<ReportDto>>() {
            @Override
            public void onResponse(Call<List<ReportDto>> call, Response<List<ReportDto>> response) {
                if (response.isSuccessful()) {
                    _reports.setValue(reportMapper.mapToReports(response.body()));
                    Log.i("DEBUG", "Retrieve report for a user : " + _reports.getValue().size());
                }else{
                    Log.i("DEBUG", "response.isNotSuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<ReportDto>> call, Throwable t) {
                Log.i("DEBUG", "onFailure: " + t.getMessage());
            }
        });
    }

    public void postReportOnWeb(Report report) {
        webService.postReport(reportMapper.mapToReportDto(report)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                if(response.isSuccessful()) {
                    Log.i("DEBUG", "Report créé");
                } else {
                    Log.i("DEBUG", "Erreur création report");
                }
                Log.i("DEBUG", response.code() + "");
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                _statusCode.setValue(500);
                Log.i("DEBUG", "onFailure: " + t.toString());
            }
        });
    }


    public LiveData<Integer> getStatusCode() {return statusCode;}
    public LiveData<List<Report>> getReports() {
        return reports;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }
}
