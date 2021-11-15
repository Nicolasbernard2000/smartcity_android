package com.example.smartcity_app.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

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
        street = (EditText) root.findViewById(R.id.create_account_edit_street);
        houseNumber = (EditText) root.findViewById(R.id.create_account_edit_house_number);
        zipCode = (EditText) root.findViewById(R.id.create_account_edit_zip_code);
        city = (EditText) root.findViewById(R.id.create_account_edit_city);
        button = (Button) root.findViewById(R.id.create_account_button);
        button.setOnClickListener(new CreateAccountListener());

        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(GregorianCalendar.YEAR);
        int month = today.get(GregorianCalendar.MONTH);
        int day = today.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birthDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear) + "/" + year);
            }
        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        return root;
    }

    private class CreateAccountListener implements View.OnClickListener {
        public CreateAccountListener() {}

        @Override
        public void onClick(View view) {
            //TODO récupérer les informations
            //TODO vérifier qu'elles ne sont pas blank
            //TODO créer un user et gérer ses exceptions
            try {
                checkForm();
            } catch (Exception e) {

                Toast toast = Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        public void checkForm() throws Exception {
            String textPassword = password.getText().toString();
            String textConfirmPassword = confirmPassword.getText().toString();
            int textZipCode = Integer.parseInt(zipCode.getText().toString());

            if(!textPassword.equals(textConfirmPassword)) {
                throw new Exception(getResources().getString(R.string.exception_password));
            }

            if(!(1000 <= textZipCode && textZipCode < 10000)) {
                throw  new Exception(getResources().getString(R.string.exception_zip_code));
            }
        }
    }
}
