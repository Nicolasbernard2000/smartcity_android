package com.example.smartcity_app.ui.loging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button connectButton;
    private Button withoutConnexionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.login_edit_email);
        password = (EditText)findViewById(R.id.login_edit_password);
        connectButton = (Button)findViewById(R.id.login_button);
        connectButton.setOnClickListener(new ConnectWithAccountListener(this));
        withoutConnexionButton = (Button)findViewById(R.id.login_without_connexion_button);
        withoutConnexionButton.setOnClickListener(new ConnectWithoutAccountListener(this));
    }

    private class ConnectWithAccountListener implements View.OnClickListener {
        private Context context;

        public ConnectWithAccountListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            // TODO : vérifier l'email et le mot de passe grâce à l'API
            // TODO : créer un model User avec ses informations et le passer via Intent
            Log.v("Debug", "Avec un compte");
            User user = new User(emailValue, passwordValue);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    private class ConnectWithoutAccountListener implements View.OnClickListener {
        private Context context;

        public ConnectWithoutAccountListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Log.v("Debug", "Sans compte");
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }
}