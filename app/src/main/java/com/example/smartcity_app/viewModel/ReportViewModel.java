package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.R;
import com.example.smartcity_app.repository.web.dto.ReportWithFilterDto;
import com.example.smartcity_app.service.mappers.ReportMapper;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.ReportDto;
import com.example.smartcity_app.util.InputCheck;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends AndroidViewModel {
    private MutableLiveData<List<Report>> _reports = new MutableLiveData<>();
    private LiveData<List<Report>> reports = _reports;

    private MutableLiveData<Integer> _statusCodeCreation = new MutableLiveData<>();
    private LiveData<Integer> statusCodeCreation = _statusCodeCreation;

    private MutableLiveData<Integer> _statusCodeDelete = new MutableLiveData<>();
    private LiveData<Integer> statusCodeDelete = _statusCodeDelete;

    private MutableLiveData<Integer> _statusCodePatch = new MutableLiveData<>();
    private LiveData<Integer> statusCodePatch = _statusCodePatch;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<HashMap<String , Integer>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, Integer>> inputErrors = _inputErrors;

    private WalloniaFixedWebService webService;
    private ReportMapper reportMapper;

    public ReportViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.reportMapper = ReportMapper.getInstance();
    }

    public void checkData(String description, String street, String houseNumber, String zipCode, String city) {
        HashMap<String, Integer> errors = new HashMap<>();

        if(!InputCheck.isInputValid(description))
            errors.put("description", R.string.error_description);

        if(!InputCheck.isInputValid(street))
            errors.put("street", R.string.error_street);

        if(!InputCheck.isHouseNumberValid(houseNumber))
            errors.put("houseNumber", R.string.error_house_number);

        if(!InputCheck.isZipCodeValid(zipCode))
            errors.put("zipCode", R.string.error_zip_code);

        if(!InputCheck.isInputValid(city))
            errors.put("city", R.string.error_city);

        _inputErrors.setValue(errors);
    }

    public void getReportsWithOffsetLimitAndFilter(Integer offset, Integer limit, String filter) {
        webService.getReportsWithOffsetLimitAndFilter(offset, limit, filter).enqueue(new Callback<ReportWithFilterDto>() {
            @Override
            public void onResponse(Call<ReportWithFilterDto> call, Response<ReportWithFilterDto> response) {
                if(response.body() != null)
                    _reports.setValue(reportMapper.mapToReports(response.body().getReportDtos()));
            }

            @Override
            public void onFailure(Call<ReportWithFilterDto> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void getReportsFromWebWithUserId(Integer id) {
        webService.getReportsWithUserId(id).enqueue(new Callback<List<ReportDto>>() {
            @Override
            public void onResponse(Call<List<ReportDto>> call, Response<List<ReportDto>> response) {
                if (response.isSuccessful()) {
                    _reports.setValue(reportMapper.mapToReports(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<ReportDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void postReportOnWeb(Report report) {
        webService.postReport(reportMapper.mapToReportDto(report)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                _statusCodeCreation.setValue(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void deleteReportOnWeb(Report report) {
        webService.deleteReport(reportMapper.mapToReportDto(report)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCodeDelete.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void modifyReportOnWeb(Report report) {
        webService.modifyReport(reportMapper.mapToReportDto(report)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCodePatch.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<Integer> getStatusCodeCreation() {
        return statusCodeCreation;
    }
    public LiveData<Integer> getStatusCodeDelete() {return statusCodeDelete;}
    public LiveData<Integer> getStatusCodePatch() {
        return statusCodePatch;
    }

    public LiveData<List<Report>> getReports() {
        return reports;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<HashMap<String, Integer>> getInputErrors() {
        return inputErrors;
    }
}
