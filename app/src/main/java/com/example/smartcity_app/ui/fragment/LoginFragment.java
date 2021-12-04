package com.example.smartcity_app.ui.fragment;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.smartcity_app.R;
import com.example.smartcity_app.mappers.UserMapper;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.dto.UserDto;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.ui.dialog.InformationDialog;
import com.example.smartcity_app.viewModels.LoginViewModel;

public class LoginFragment extends Fragment {
    private EditText email;
    private EditText password;
    private Button connectButton;
    private Button createAccountButton;
    private LoginViewModel loginViewModel;
    private ViewGroup container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);
        this.container = container;

        if (MainActivity.isUserConnected()) {
            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_profile);
        }

        email = (EditText) root.findViewById(R.id.login_edit_email);
        password = (EditText) root.findViewById(R.id.login_edit_password);

        connectButton = (Button) root.findViewById(R.id.login_button);
        connectButton.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();

            loginViewModel.log(emailText, passwordText);
        });

        createAccountButton = (Button) root.findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_create_account);
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getToken().observe(getViewLifecycleOwner(), token -> {
            DecodedJWT decodedJWT = JWT.decode(token);
            Claim userData = decodedJWT.getClaim("user");
            UserDto userDto = userData.as(UserDto.class);
            User user = UserMapper.getInstance().mapToUser(userDto);
            MainActivity.setUser(user);

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.token), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.token), token);
            editor.commit();

            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_profile);
        });

        loginViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });

        loginViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
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