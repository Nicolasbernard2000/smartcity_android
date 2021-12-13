package com.example.smartcity_app.view.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
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

public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ImageView image;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        linearLayout = (LinearLayout)findViewById(R.id.welcome_linear_layout);
        image = (ImageView)findViewById(R.id.waiting_image);
        button = (Button)findViewById(R.id.welcome_button);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.main_appearance_animation);
        linearLayout.startAnimation(animation);

        Animation waitingAnimation = AnimationUtils.loadAnimation(this, R.anim.waiting_animation);
        image.startAnimation(waitingAnimation);

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