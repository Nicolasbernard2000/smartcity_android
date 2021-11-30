package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.mappers.UserMapper;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.UserDto;
import com.example.smartcity_app.ui.fragment.LoginFragment;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<Integer> _idNewUser = new MutableLiveData<>();
    private LiveData<Integer> idNewUser = _idNewUser;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private WalloniaFixedWebService webService;
    private UserMapper userMapper;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.userMapper = UserMapper.getInstance();
    }

    public void postUserOnWeb(User user) {
        webService.postUser(userMapper.mapToUserDto(user)).enqueue(new Callback<UserDto>() {

            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if(response.isSuccessful()) {
                    Log.i("DEBUG", "User créé");
                    _idNewUser.setValue(response.body().getId());
                } else if(response.code() == 404){
                    Log.i("DEBUG", "Adresse mail déjà utilisée");
                }
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                _statusCode.setValue(500);
                Log.i("DEBUG", "onFailure: " + t.toString());
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }

    public LiveData<Integer> getIdNewUser(){
        return idNewUser;
    }
}
