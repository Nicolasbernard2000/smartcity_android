package com.example.smartcity_app.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.R;
import com.example.smartcity_app.mappers.UserMapper;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.RetrofitConfigurationService;
import com.example.smartcity_app.repositories.web.WalloniaFixedWebService;
import com.example.smartcity_app.repositories.web.dto.UserDto;
import com.example.smartcity_app.ui.fragment.LoginFragment;
import com.example.smartcity_app.utils.InputCheck;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

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

    private MutableLiveData<HashMap<String , String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private WalloniaFixedWebService webService;
    private UserMapper userMapper;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(application).webService();
        this.userMapper = UserMapper.getInstance();
    }

    public void checkData(String firstName, String lastName, String email, String password, String confirmPassword,
                          String birthDate, String street, String houseNumber, String zipCode, String city) {
        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isInputValid(firstName))
            errors.put("firstName", getApplication().getResources().getString(R.string.error_first_name));

        if(!InputCheck.isInputValid(lastName))
            errors.put("lastName", getApplication().getResources().getString(R.string.error_last_name));

        if(!InputCheck.isEmailValid(email))
            errors.put("email", getApplication().getResources().getString(R.string.error_email));

        if(!InputCheck.isPasswordValid(password, confirmPassword))
            errors.put("password", getApplication().getResources().getString(R.string.error_password));

        if(!InputCheck.isBirthDateValid(birthDate))
            errors.put("birthDate", getApplication().getResources().getString(R.string.error_birthdate));

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

    public void postUserOnWeb(User user) {
        webService.postUser(userMapper.mapToUserDto(user)).enqueue(new Callback<UserDto>() {

            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if(response.isSuccessful()) {
                    _idNewUser.setValue(response.body().getId());
                }
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                _statusCode.setValue(500);
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

    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }
}
