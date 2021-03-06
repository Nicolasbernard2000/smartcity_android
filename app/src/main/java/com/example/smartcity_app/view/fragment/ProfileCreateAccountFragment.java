package com.example.smartcity_app.view.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.viewModel.UserViewModel;

import java.util.Arrays;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ProfileCreateAccountFragment extends Fragment {
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private EditText birthDateEditText, streetEditText, houseNumberEditText, zipCodeEditText, cityEditText;
    private Button button;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_create_account_fragment, container, false);

        firstNameEditText = root.findViewById(R.id.create_account_edit_first_name);
        lastNameEditText = root.findViewById(R.id.create_account_edit_last_name);
        emailEditText = root.findViewById(R.id.create_account_edit_email);
        passwordEditText = root.findViewById(R.id.create_account_edit_password);
        confirmPasswordEditText = root.findViewById(R.id.create_account_edit_confirm_password);
        birthDateEditText = root.findViewById(R.id.create_account_edit_birthdate);
        streetEditText = root.findViewById(R.id.create_account_edit_street);
        houseNumberEditText = root.findViewById(R.id.create_account_edit_house_number);
        zipCodeEditText = root.findViewById(R.id.create_account_edit_zip_code);
        cityEditText = root.findViewById(R.id.create_account_edit_city);
        button = root.findViewById(R.id.create_account_button);

        button.setOnClickListener(v -> {
            checkData();

            if(button.isEnabled()) {
                List<String> date = Arrays.asList(birthDateEditText.getText().toString().split("/"));
                int year = Integer.parseInt(date.get(2));
                int month = Integer.parseInt(date.get(1));
                int day = Integer.parseInt(date.get(0));
                GregorianCalendar birthDateCalendar = new GregorianCalendar(year, month - 1, day, 12, 0, 0);
                Date birthDateDate = new Date(birthDateCalendar.getTimeInMillis());
                String emailText = emailEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();
                String firstnameText = firstNameEditText.getText().toString();
                String lastnameText = lastNameEditText.getText().toString();
                String cityText = cityEditText.getText().toString();
                String streetText = streetEditText.getText().toString();
                Integer zipCodeInteger = Integer.parseInt(zipCodeEditText.getText().toString());
                Integer houseNumberInteger = Integer.parseInt(houseNumberEditText.getText().toString());

                User newUser = new User(emailText, passwordText, firstnameText, lastnameText, birthDateDate, cityText, streetText, zipCodeInteger, houseNumberInteger);
                userViewModel.postUserOnWeb(newUser);

                button.setEnabled(false);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!button.isEnabled())
                    checkData();
            }
        };

        firstNameEditText.addTextChangedListener(textWatcher);
        lastNameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        confirmPasswordEditText.addTextChangedListener(textWatcher);
        birthDateEditText.addTextChangedListener(textWatcher);
        streetEditText.addTextChangedListener(textWatcher);
        houseNumberEditText.addTextChangedListener(textWatcher);
        zipCodeEditText.addTextChangedListener(textWatcher);
        cityEditText.addTextChangedListener(textWatcher);

        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(GregorianCalendar.YEAR);
        int month = today.get(GregorianCalendar.MONTH);
        int day = today.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthDateEditText.setText(new StringBuilder().append(String.format("%02d", dayOfMonth)).append("/").append(String.format("%02d", month + 1)).append("/").append(year).toString());
            }
        };

        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getInputErrors().observe(getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                firstNameEditText.setError(inputErrors.containsKey("firstName") ? getString(inputErrors.get("firstName")) : null);
                lastNameEditText.setError(inputErrors.containsKey("lastName") ? getString(inputErrors.get("lastName")) : null);
                emailEditText.setError(inputErrors.containsKey("email") ? getString(inputErrors.get("email")) : null);
                confirmPasswordEditText.setError(inputErrors.containsKey("password") ? getString(inputErrors.get("password")) : null);
                birthDateEditText.setError(inputErrors.containsKey("birthDate") ? getString(inputErrors.get("birthDate")) : null);
                streetEditText.setError(inputErrors.containsKey("street") ? getString(inputErrors.get("street")) : null);
                houseNumberEditText.setError(inputErrors.containsKey("houseNumber") ? getString(inputErrors.get("houseNumber")) : null);
                zipCodeEditText.setError(inputErrors.containsKey("zipCode") ? getString(inputErrors.get("zipCode")) : null);
                cityEditText.setError(inputErrors.containsKey("city") ? getString(inputErrors.get("city")) : null);
            }
            button.setEnabled(inputErrors.isEmpty());
        });

        userViewModel.getStatusCodeCreation().observe(getViewLifecycleOwner(), code -> {
            int typeMessage;
            int message;
            switch(code) {
                case 201:
                    typeMessage = R.string.success;
                    message = R.string.user_created;
                    Navigation.findNavController(requireView()).navigate(R.id.fragment_login);
                    break;
                case 400:
                    typeMessage = R.string.error;
                    message = R.string.email_already_used;
                    break;
                case 500:
                    typeMessage = R.string.error;
                    message = R.string.error_servor;
                    break;
                default:
                    typeMessage = R.string.error;
                    message = R.string.error_unknown;
            }
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(typeMessage, message);
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            button.setEnabled(true);
        });

        userViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            button.setEnabled(false);
        });
    }

    public void checkData(){
        userViewModel.checkData(
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString(),
                passwordEditText.getText().toString(),
                confirmPasswordEditText.getText().toString(),
                birthDateEditText.getText().toString(),
                streetEditText.getText().toString(),
                houseNumberEditText.getText().toString(),
                zipCodeEditText.getText().toString(),
                cityEditText.getText().toString()
        );
    }
}