package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.mappers.ReportMapper;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.ReportDto;
import com.example.smartcity_app.ui.fragment.LoginFragment;
import com.example.smartcity_app.ui.fragment.ReportCreationFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends AndroidViewModel {
    private MutableLiveData<List<Report>> _reports = new MutableLiveData<>();
    private LiveData<List<Report>> reports = _reports;

    private MutableLiveData<Integer> _idReport = new MutableLiveData<>();
    private LiveData<Integer> idReport = _idReport;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private WalloniaFixedWebService webService;
    private ReportMapper reportMapper;

    public ReportViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.reportMapper = ReportMapper.getInstance();
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

    public LiveData<List<Report>> getReports() {
        return reports;
    }

    public void postReportOnWeb(Report report) {
        webService.postReport(reportMapper.mapToReportDto(report)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                if(response.isSuccessful()) {
                    ReportCreationFragment.setToast("Report créé");
                    Log.i("DEBUG", "Report créé");
                } else {
                    ReportCreationFragment.setToast("Erreur création report");
                    Log.i("DEBUG", "Erreur création report");
                }
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                ReportCreationFragment.setToast("Erreur création report");
                Log.i("DEBUG", "onFailure: " + t.toString());
            }
        });
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
