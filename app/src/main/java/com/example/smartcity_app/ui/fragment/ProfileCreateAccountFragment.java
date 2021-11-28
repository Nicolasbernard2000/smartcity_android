package com.example.smartcity_app.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.viewModels.UserViewModel;

import java.util.Arrays;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
    private UserViewModel userViewModel;
    private Context context;
    private ViewGroup container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_create_account_fragment, container, false);
        this.context = getContext();
        this.container = container;

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
                datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        return root;
    }

    private class CreateAccountListener implements View.OnClickListener {
        public CreateAccountListener() {}

        @Override
        public void onClick(View view) {
            String firstNameText = firstName.getText().toString();
            String lastNameText = lastName.getText().toString();
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            String birthDateText = birthDate.getText().toString();
            String streetText = street.getText().toString();
            String cityText = city.getText().toString();

            try {
                List<String> test = Arrays.asList(birthDateText.split("/"));
                int year = Integer.parseInt(test.get(2));
                int month = Integer.parseInt(test.get(1));
                int day = Integer.parseInt(test.get(0));
                GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, day);
                Date birthDateDate = new Date(gregorianCalendar.getTimeInMillis());
                Integer houseNumberInteger = Integer.parseInt(houseNumber.getText().toString());
                Integer zipCodeInteger = Integer.parseInt(zipCode.getText().toString());

                checkForm();

                User user = new User(emailText, passwordText, firstNameText, lastNameText, birthDateDate, cityText, streetText, zipCodeInteger, houseNumberInteger);
                userViewModel.postUserOnWeb(user);
                userViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
                    String message;
                    switch(code) {
                        case 200:
                            message = 200 + getString(R.string.user_created);
                            userViewModel.getIdNewUser().observe(getViewLifecycleOwner(), user::setId);
                            MainActivity.setUser(user);
                            NavController navController = Navigation.findNavController(container);
                            navController.navigate(R.id.fragment_profile);
                            break;
                        case 404:
                            message = 404 + getString(R.string.email_already_used);
                            break;
                        case 500:
                            message = 500 + getString(R.string.error_servor);
                            break;
                        default:
                            message = getString(R.string.error_unknown);
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                });
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
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
