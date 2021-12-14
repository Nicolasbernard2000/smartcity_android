package com.example.smartcity_app.view.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.smartcity_app.R;
import com.example.smartcity_app.view.MainActivity;

import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ImageView image;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        button = (Button)findViewById(R.id.welcome_button);
        button.setOnClickListener(new ChangeActivityListener(this));
    }

    private class ChangeActivityListener implements OnClickListener {
        private Context context;

        public ChangeActivityListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Log.v("Debug", "Bouton Welcome");
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }
}