package com.example.smartcity_app.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.ui.MainActivity;


public class LoginFragment extends Fragment {
    private EditText email;
    private EditText password;
    private Button connectButton;
    private Button createAccountButton;
    private ViewGroup container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);

        if (MainActivity.isUserConnected()) {
            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_profile);
        }

        this.container = container;
        email = (EditText) root.findViewById(R.id.login_edit_email);
        password = (EditText) root.findViewById(R.id.login_edit_password);
        connectButton = (Button) root.findViewById(R.id.login_button);
        connectButton.setOnClickListener(new ConnectWithAccountListener());
        createAccountButton = (Button) root.findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new CreateAccountListener());
        return root;
    }

    private class ConnectWithAccountListener implements View.OnClickListener {
        public ConnectWithAccountListener() {
        }

        @Override
        public void onClick(View v) {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            // TODO : vérifier l'email et le mot de passe grâce à l'API
            // TODO : créer un model User avec ses informations et le passer via Intent
            Log.v("Debug", "Connection avec un compte");
            try {
                User user = new User(emailValue, passwordValue);
                MainActivity.setUser(user);
                NavController navController = Navigation.findNavController(container);
                navController.navigate(R.id.fragment_profile);
            } catch (Exception e) {
                Toast toast = Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private class CreateAccountListener implements View.OnClickListener {

        public CreateAccountListener() {
        }

        @Override
        public void onClick(View view) {
            Log.v("Debug", "Création d'un compte");
            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_create_account);
        }
    }
}