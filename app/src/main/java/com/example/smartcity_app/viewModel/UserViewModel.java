package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity_app.R;
import com.example.smartcity_app.service.mappers.UserMapper;
import com.example.smartcity_app.model.NetworkError;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repository.web.RetrofitConfigurationService;
import com.example.smartcity_app.repository.web.WalloniaFixedWebService;
import com.example.smartcity_app.repository.web.dto.UserDto;
import com.example.smartcity_app.util.InputCheck;
import com.example.smartcity_app.util.errors.NoConnectivityException;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> _statusCodeCreation = new MutableLiveData<>();
    private LiveData<Integer> statusCodeCreation = _statusCodeCreation;

    private MutableLiveData<Integer> _statusCodeModification = new MutableLiveData<>();
    private LiveData<Integer> statusCodeModification = _statusCodeModification;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

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

    public void checkData(String firstName, String lastName, String street, String houseNumber, String zipCode, String city, String birthDate) {
        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isInputValid(firstName))
            errors.put("firstName", getApplication().getResources().getString(R.string.error_first_name));

        if(!InputCheck.isInputValid(lastName))
            errors.put("lastName", getApplication().getResources().getString(R.string.error_last_name));

        if(!InputCheck.isInputValid(street))
            errors.put("street", getApplication().getResources().getString(R.string.error_street));

        if(!InputCheck.isHouseNumberValid(houseNumber))
            errors.put("houseNumber", getApplication().getResources().getString(R.string.error_house_number));

        if(!InputCheck.isZipCodeValid(zipCode))
            errors.put("zipCode", getApplication().getResources().getString(R.string.error_zip_code));

        if(!InputCheck.isInputValid(city))
            errors.put("city", getApplication().getResources().getString(R.string.error_city));

        if(!InputCheck.isBirthDateValid(birthDate))
            errors.put("birthDate", getApplication().getResources().getString(R.string.error_birthdate));

        _inputErrors.setValue(errors);
    }

    public void postUserOnWeb(User user) {
        webService.postUser(userMapper.mapToUserDto(user)).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                _statusCodeCreation.setValue(response.code());
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void modifyUserOnWeb(User user) {
        webService.modifyAccount(userMapper.mapToUserDto(user)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                _statusCodeModification.setValue(response.code());
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
    public LiveData<Integer> getStatusCodeModification() {
        return statusCodeModification;
    }

    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
