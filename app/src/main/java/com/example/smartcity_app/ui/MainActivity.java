package com.example.smartcity_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.ui.welcome.WelcomeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        if(getIntent().hasExtra("user")) {
            user = (User)getIntent().getSerializableExtra("user");
        } else {
            user = null;
        }
    }

    public static User getUser() {
        return user;
    }

    public static boolean isUserConnected() {
        return user != null;
    }
}