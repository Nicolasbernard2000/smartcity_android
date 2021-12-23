package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.LoginDto;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> _token = new MutableLiveData<>();
    private LiveData<String> token = _token;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private WalloniaFixedWebService webService;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(application).webService();
    }

    public void log(String email, String password) {
        webService.log(new LoginDto(email, password)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if(response.isSuccessful())
                    _token.setValue(response.body());
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<String> getToken() {
        return token;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }
}
