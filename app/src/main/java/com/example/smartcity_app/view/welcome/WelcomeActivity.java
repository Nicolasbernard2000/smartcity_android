package com.example.smartcity_app.view.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.view.MainActivity;

public class WelcomeActivity extends AppCompatActivity {
    private Button button;
    private ImageView bergImage;
    private ImageView bernardImage;
    private ImageView henalluxImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        button = (Button)findViewById(R.id.welcome_button);
        bergImage = (ImageView)findViewById(R.id.image_berg_thibaut);
        bernardImage = (ImageView)findViewById(R.id.image_bernard_nicolas);
        henalluxImage = (ImageView)findViewById(R.id.image_henallux);

        button.setOnClickListener(new ChangeActivityListener(this));
        bergImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/thibautberg"));
                startActivity(browserIntent);
            }
        });

        bernardImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/nicolas-bernico-bernard"));
                startActivity(browserIntent);
            }
        });

        henalluxImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.henallux.be/"));
                startActivity(browserIntent);
            }
        });
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