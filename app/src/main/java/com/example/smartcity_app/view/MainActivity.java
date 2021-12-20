package com.example.smartcity_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smartcity_app.R;
import com.example.smartcity_app.service.mappers.UserMapper;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repository.web.dto.UserDto;
import com.example.smartcity_app.view.dialog.SettingsDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static User user = null;
    private BottomNavigationView bottomNavigationView;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            SettingsDialog settingsDialog = SettingsDialog.getInstance(this);
            settingsDialog.show(getSupportFragmentManager(), null);
        });

        checkToken();

        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.fragment_profile);
        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void checkToken() {
        //Retrieve the token, check is expiration date and create the session user if the token is still good
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.token), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.token), null);

        if(token != null) {
            DecodedJWT decodedJWT = JWT.decode(token);
            Date expirationDate = decodedJWT.getExpiresAt();
            Date today = new Date();
            boolean isDateExpired = expirationDate.before(today);
            if(!isDateExpired) {
                Claim userData = decodedJWT.getClaim("user");
                UserDto userDto = userData.as(UserDto.class);
                User user = UserMapper.getInstance().mapToUser(userDto);
                setUser(user);
            }
        }
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MainActivity.user = user;
    }

    public static boolean isUserConnected() {
        return user != null;
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}