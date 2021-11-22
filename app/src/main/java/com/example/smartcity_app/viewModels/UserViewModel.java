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

    private WalloniaFixedWebService webService;
    private UserMapper userMapper;


    public UserViewModel(@NonNull Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.userMapper = UserMapper.getInstance();
    }

    public void getUserFromWeb(int userId) {
        webService.getUser(userId).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(@NotNull Call<UserDto> call, @NotNull Response<UserDto> response) {
                if(response.isSuccessful()){
                    _user.setValue(userMapper.mapToUser(response.body()));
                    LoginFragment.setUserFromAPI(user.getValue());
                    Log.i("DEBUG", "response isSuccessful : " + _user.getValue());
                } else{
                    Log.i("DEBUG", "response.isNotSuccessful");
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Log.v("DEBUG", "ERREUR");
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }
}
