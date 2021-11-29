package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smartcity_app.mappers.UserMapper;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.LoginDto;
import com.example.smartcity_app.repositories.web.dto.UserDto;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private String token;

    private WalloniaFixedWebService webService;
    private UserMapper userMapper;


    public LoginViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.userMapper = UserMapper.getInstance();
    }

    public void log(String email, String password) {
        webService.log(new LoginDto(email, password)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if(response.isSuccessful()) {
                    token = response.body();
                    DecodedJWT decodedJWT = JWT.decode(token);
                    Claim userData = decodedJWT.getClaim("user");
                    UserDto userDto = userData.as(UserDto.class);
                    User user = userMapper.mapToUser(userDto);
                    _user.setValue(user);
                } else {
                    Log.i("Debug", "Erreur login");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("Debug", "Erreur login");
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }
}
