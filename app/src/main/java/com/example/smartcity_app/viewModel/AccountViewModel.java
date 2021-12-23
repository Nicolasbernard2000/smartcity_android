package com.example.smartcity_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repository.web.dto.UserDto;
import com.example.smartcity_app.service.mappers.UserMapper;

import java.util.Date;

public class AccountViewModel extends AndroidViewModel {
    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    public AccountViewModel(@NonNull Application application) {
        super(application);
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

    public LiveData<User> getUser() {
        return user;
    }
}
