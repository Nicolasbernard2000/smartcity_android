package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.service.mappers.ReportTypeMapper;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.ReportTypeDto;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportTypeViewModel extends AndroidViewModel {
    private MutableLiveData<List<ReportType>> _reportTypes = new MutableLiveData<>();
    private LiveData<List<ReportType>> reportTypes = _reportTypes;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private WalloniaFixedWebService webService;
    private ReportTypeMapper reportTypeMapper;

    public ReportTypeViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.reportTypeMapper = ReportTypeMapper.getInstance();
    }

    public void getReportTypesFromWeb() {
        webService.getReportTypes().enqueue(new Callback<List<ReportTypeDto>>() {
            @Override
            public void onResponse(Call<List<ReportTypeDto>> call, Response<List<ReportTypeDto>> response) {
                if(response.isSuccessful()) {
                    _reportTypes.setValue(reportTypeMapper.mapToReportTypes(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<ReportTypeDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<List<ReportType>> getReportTypes() {
        return reportTypes;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
}
