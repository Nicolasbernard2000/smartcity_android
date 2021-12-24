package com.example.smartcity_app.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.util.CallbackUserModify;
import com.example.smartcity_app.util.Constants;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.view.dialog.UserModifyDialog;
import com.example.smartcity_app.viewModel.AccountViewModel;
import com.example.smartcity_app.viewModel.UserViewModel;

import java.sql.Date;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public class ProfileInformationFragment extends Fragment implements CallbackUserModify {
    private EditText firstName, lastName, email, street, houseNumber, zipCode, city, birthDate;
    private Button disconnectionButton, modificationButton;
    private User user;
    private ViewGroup container;
    private UserViewModel userViewModel;
    private AccountViewModel accountViewModel;
    private String token;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_information_fragment, container, false);
        this.container = container;

        sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        firstName = root.findViewById(R.id.profile_edit_first_name);
        lastName = root.findViewById(R.id.profile_edit_last_name);
        email = root.findViewById(R.id.profile_edit_email);
        street = root.findViewById(R.id.profile_edit_street);
        houseNumber = root.findViewById(R.id.profile_edit_house_number);
        zipCode = root.findViewById(R.id.profile_edit_zip_code);
        city = root.findViewById(R.id.profile_edit_city);
        birthDate = root.findViewById(R.id.profile_edit_birthdate);
        disconnectionButton = root.findViewById(R.id.profile_disconnection_button);
        modificationButton = root.findViewById(R.id.profile_modification_button);

        disconnectionButton.setOnClickListener(new ProfileInformationFragment.DisconnectListener());
        modificationButton.setOnClickListener(new ProfileInformationFragment.ModifyListener(this));

        GregorianCalendar today = new GregorianCalendar();
        int todayYear = today.get(GregorianCalendar.YEAR);
        int todayMonth = today.get(GregorianCalendar.MONTH);
        int todayDay = today.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthDate.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month + 1) + "/" + year);
            }
        };

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, todayYear, todayMonth, todayDay);
                datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
                datePickerDialog.show();
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
                if(!modificationButton.isEnabled())
                    checkData();
            }
        };

        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        birthDate.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        houseNumber.addTextChangedListener(textWatcher);
        zipCode.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            GregorianCalendar birthDateTemp = new GregorianCalendar();
            birthDateTemp.setTime(user.getBirthDate());
            int day = birthDateTemp.get(GregorianCalendar.DAY_OF_MONTH);
            int month = birthDateTemp.get(GregorianCalendar.MONTH) + 1;
            int year = birthDateTemp.get(GregorianCalendar.YEAR);
            String dateString = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year;

            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            email.setText(user.getEmail());
            street.setText(user.getStreet());
            houseNumber.setText(user.getHouseNumber().toString());
            zipCode.setText(user.getZipCode().toString());
            city.setText(user.getCity());
            birthDate.setText(dateString);
        });

        if(token != null) {
            accountViewModel.getUserFromToken(token);
        }

        userViewModel.getStatusCodeModification().observe(getViewLifecycleOwner(), code -> {
            int typeMessage;
            int message;
            switch(code) {
                case 204:
                    typeMessage = R.string.success;
                    message = R.string.user_modified;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.TOKEN, null);
                    editor.commit();
                    NavController navController = Navigation.findNavController(container);
                    navController.navigate(R.id.fragment_login);
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
            modificationButton.setEnabled(true);
        });

        userViewModel.getInputErrors().observe(getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                firstName.setError(inputErrors.containsKey("firstName") ? getString(inputErrors.get("firstName")) : null);
                lastName.setError(inputErrors.containsKey("lastName") ? getString(inputErrors.get("lastName")) : null);
                birthDate.setError(inputErrors.containsKey("birthDate") ? getString(inputErrors.get("birthDate")) : null);
                street.setError(inputErrors.containsKey("street") ? getString(inputErrors.get("street")) : null);
                houseNumber.setError(inputErrors.containsKey("houseNumber") ? getString(inputErrors.get("houseNumber")) : null);
                zipCode.setError(inputErrors.containsKey("zipCode") ? getString(inputErrors.get("zipCode")) : null);
                city.setError(inputErrors.containsKey("city") ? getString(inputErrors.get("city")) : null);
            }
            modificationButton.setEnabled(inputErrors.isEmpty());
        });

        userViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            modificationButton.setEnabled(false);
        });
    }

    private class DisconnectListener implements View.OnClickListener {
        public DisconnectListener() {}

        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.TOKEN, null);
            editor.commit();

            NavController navController = Navigation.findNavController(container);
            navController.navigate(R.id.fragment_login);
        }
    }

    private class ModifyListener implements View.OnClickListener {
        private Fragment fragment;

        public ModifyListener(Fragment fragment){
            this.fragment = fragment;
        }

        @Override
        public void onClick(View view) {
            checkData();

            if(modificationButton.isEnabled()) {
                List<String> date = Arrays.asList(birthDate.getText().toString().split("/"));
                int year = Integer.parseInt(date.get(2));
                int month = Integer.parseInt(date.get(1));
                int day = Integer.parseInt(date.get(0));
                GregorianCalendar birthDateCalendar = new GregorianCalendar(year, month - 1, day, 12, 0, 0);
                Date birthDateDate = new Date(birthDateCalendar.getTimeInMillis());
                String firstnameText = firstName.getText().toString();
                String lastnameText = lastName.getText().toString();
                String cityText = city.getText().toString();
                String streetText = street.getText().toString();
                Integer zipCodeInteger = Integer.parseInt(zipCode.getText().toString());
                Integer houseNumberInteger = Integer.parseInt(houseNumber.getText().toString());

                user.setFirstName(firstnameText);
                user.setLastName(lastnameText);
                user.setEmail(null);
                user.setBirthDate(birthDateDate);
                user.setCity(cityText);
                user.setStreet(streetText);
                user.setZipCode(zipCodeInteger);
                user.setHouseNumber(houseNumberInteger);

                UserModifyDialog userModifyDialog = UserModifyDialog.getInstance();
                userModifyDialog.setTargetFragment(fragment, 0);
                userModifyDialog.setUser(user);
                userModifyDialog.show(fragment.getParentFragmentManager(), null);
            }
            modificationButton.setEnabled(false);
        }
    }

    @Override
    public void modifyUser(User user) {
        userViewModel.modifyUserOnWeb(user);
    }

    public void checkData(){
        userViewModel.checkData(
                firstName.getText().toString(),
                lastName.getText().toString(),
                street.getText().toString(),
                houseNumber.getText().toString(),
                zipCode.getText().toString(),
                city.getText().toString(),
                birthDate.getText().toString()
        );
    }
}
