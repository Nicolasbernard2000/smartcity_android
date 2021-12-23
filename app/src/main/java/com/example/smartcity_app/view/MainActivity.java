package com.example.smartcity_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.smartcity_app.R;
import com.example.smartcity_app.view.dialog.SettingsDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            SettingsDialog settingsDialog = SettingsDialog.getInstance(this);
            settingsDialog.show(getSupportFragmentManager(), null);
        });

        bottomNavigationView = findViewById(R.id.activity_main_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.fragment_profile);
        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}