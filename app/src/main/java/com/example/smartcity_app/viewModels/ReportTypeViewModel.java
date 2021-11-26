package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.mappers.ReportTypeMapper;
import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.ReportTypeDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportTypeViewModel extends AndroidViewModel {
    private MutableLiveData<List<ReportType>> _reportTypes = new MutableLiveData<>();
    private LiveData<List<ReportType>> reportTypes = _reportTypes;

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
                    Log.i("DEBUG", "response.isSuccessful dataCount : " + _reportTypes.getValue().size());
                } else {
                    Log.i("DEBUG", "response.isNotSuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<ReportTypeDto>> call, Throwable t) {
                Log.i("DEBUG", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<List<ReportType>> getReportTypes() {
        return reportTypes;
    }
}
