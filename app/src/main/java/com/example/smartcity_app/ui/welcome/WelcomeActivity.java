package com.example.smartcity_app.ui.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.smartcity_app.R;
import com.example.smartcity_app.ui.MainActivity;

public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        linearLayout = (LinearLayout)findViewById(R.id.welcome_linear_layout);
        button = (Button)findViewById(R.id.welcome_button);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.main_appearance_animation);
        linearLayout.startAnimation(animation);

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