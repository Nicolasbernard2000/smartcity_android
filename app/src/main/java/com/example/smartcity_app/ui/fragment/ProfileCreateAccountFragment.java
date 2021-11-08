package com.example.smartcity_app.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;

public class ProfileCreateAccountFragment extends Fragment {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText birthDate;
    private EditText street;
    private EditText houseNumber;
    private EditText zipCode;
    private EditText city;
    private Button button;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_create_account_fragment, container, false);

        firstName = (EditText) root.findViewById(R.id.create_account_edit_first_name);
        lastName = (EditText) root.findViewById(R.id.create_account_edit_last_name);
        email = (EditText) root.findViewById(R.id.create_account_edit_email);
        password = (EditText)root.findViewById(R.id.create_account_edit_password);
        confirmPassword = (EditText)root.findViewById(R.id.create_account_edit_confirm_password);
        birthDate = (EditText) root.findViewById(R.id.create_account_edit_birthdate);


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, 2021, 11, 8);
                datePickerDialog.show();
            }
        });

        street = (EditText) root.findViewById(R.id.create_account_edit_street);
        houseNumber = (EditText) root.findViewById(R.id.create_account_edit_house_number);
        zipCode = (EditText) root.findViewById(R.id.create_account_edit_zip_code);
        city = (EditText) root.findViewById(R.id.create_account_edit_city);
        button = (Button) root.findViewById(R.id.create_account_button);
        button.setOnClickListener(new CreateAccountListener());

        return root;
    }

    private class CreateAccountListener implements View.OnClickListener {
        public CreateAccountListener() {}

        @Override
        public void onClick(View view) {
            //TODO vérification des informations passées
        }
    }
}
