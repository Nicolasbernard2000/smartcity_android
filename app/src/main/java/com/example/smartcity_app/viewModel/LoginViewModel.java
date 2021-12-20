package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.LoginDto;
import com.example.smartcity_app.repository.web.dto.UserDto;
import com.example.smartcity_app.service.mappers.UserMapper;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<String> _token = new MutableLiveData<>();
    private LiveData<String> token = _token;

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

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

    public void getUserFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        Date today = new Date();
        boolean isDateExpired = expirationDate.before(today);
        if(!isDateExpired) {
            Claim userData = decodedJWT.getClaim("user");
            UserDto userDto = userData.as(UserDto.class);
            User user = UserMapper.getInstance().mapToUser(userDto);
            _user.setValue(user);
        } else {
            _user.setValue(null);
        }
    }

    public LiveData<String> getToken() {
        return token;
    }
    public LiveData<User> getUser() {
        return user;
    }
    public LiveData<NetworkError> getError() {
        return error;
    }
    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }
}
