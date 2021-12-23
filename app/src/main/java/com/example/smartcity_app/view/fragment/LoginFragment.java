package com.example.smartcity_app.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartcity_app.R;
import com.example.smartcity_app.util.Constants;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.viewModel.AccountViewModel;
import com.example.smartcity_app.viewModel.LoginViewModel;

public class LoginFragment extends Fragment {
    private EditText email;
    private EditText password;
    private Button connectButton;
    private Button createAccountButton;
    private AccountViewModel accountViewModel;
    private LoginViewModel loginViewModel;
    private ViewGroup container;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);
        this.container = container;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        email = root.findViewById(R.id.login_edit_email);
        password = root.findViewById(R.id.login_edit_password);
        connectButton = root.findViewById(R.id.login_button);
        createAccountButton = root.findViewById(R.id.create_account_button);

        connectButton.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            connectButton.setEnabled(false);
            loginViewModel.log(emailText, passwordText);
        });

        createAccountButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_create_account);
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        if(token != null) {
            accountViewModel.getUserFromToken(token);
        }
        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null) {
                NavController navController = Navigation.findNavController(container);
                navController.navigate(R.id.fragment_profile);
            }
        });

        loginViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            accountViewModel.getUserFromToken(token);

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.TOKEN, token);
            editor.commit();

            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_profile);
        });

        loginViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            connectButton.setEnabled(true);
        });

        loginViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            connectButton.setEnabled(true);
            if(code == 404) {
                informationDialog.setInformation(R.string.error, R.string.wrong_email_password);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            } else if(code == 500) {
                informationDialog.setInformation(R.string.error, R.string.error_servor);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });
    }
}