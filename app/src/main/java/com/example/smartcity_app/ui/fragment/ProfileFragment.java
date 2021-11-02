package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.ui.MainActivity;

public class ProfileFragment extends Fragment {
    private TextView information;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText street;
    private EditText houseNumber;
    private EditText zipCode;
    private EditText city;
    private EditText password;
    private EditText birthDate;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        user = MainActivity.getUser();
        Log.v("Debug", MainActivity.isUserConnected() ? "Bon ici":"VTFF");
        information = (TextView)root.findViewById(R.id.profile_information);

        firstName = (EditText) root.findViewById(R.id.profile_edit_first_name);
        lastName = (EditText) root.findViewById(R.id.profile_edit_last_name);
        email = (EditText) root.findViewById(R.id.profile_edit_email);
        street = (EditText) root.findViewById(R.id.profile_edit_street);
        houseNumber = (EditText) root.findViewById(R.id.profile_edit_house_number);
        zipCode = (EditText) root.findViewById(R.id.profile_edit_zip_code);
        city = (EditText) root.findViewById(R.id.profile_edit_city);
        password = (EditText)root.findViewById(R.id.profile_edit_password);
        birthDate = (EditText) root.findViewById(R.id.profile_edit_birthdate);

        String info = user.getLastName().toUpperCase() + " " + user.getFirstName();
        information.setText(info);
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        street.setText(user.getStreet());
        houseNumber.setText(user.getHouseNumber().toString());
        zipCode.setText(user.getZipCode().toString());
        city.setText(user.getCity());
        password.setText(user.getPassword());
        birthDate.setText(user.getBirthDate().toString());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
