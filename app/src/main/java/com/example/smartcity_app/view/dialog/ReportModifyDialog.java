package com.example.smartcity_app.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.util.CallbackReportModify;
import com.example.smartcity_app.util.Constants;
import com.example.smartcity_app.viewModel.AccountViewModel;
import com.example.smartcity_app.viewModel.ReportViewModel;

public class ReportModifyDialog extends DialogFragment {
    private static ReportModifyDialog instance;
    private CallbackReportModify host;
    private Report report;
    private ReportViewModel reportViewModel;
    private AccountViewModel accountViewModel;
    private Spinner reportType;
    private TextView reportText;
    private TextView description;
    private TextView street;
    private TextView houseNumber;
    private TextView zipCode;
    private TextView city;
    private Button modifyReportButton;
    private Context context;
    private User user;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        host = (CallbackReportModify) getTargetFragment();
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.report_operation_fragment, null);

        reportText = view.findViewById(R.id.text_report);
        reportType = view.findViewById(R.id.create_report_report_type);
        reportType.setVisibility(View.INVISIBLE);
        description = view.findViewById(R.id.create_report_description);
        street = view.findViewById(R.id.create_report_street);
        houseNumber = view.findViewById(R.id.create_report_house_number);
        zipCode = view.findViewById(R.id.create_report_zip_code);
        city = view.findViewById(R.id.create_report_city);
        modifyReportButton = view.findViewById(R.id.report_button);

        reportText.setText(R.string.report_modify);
        description.setText(report.getDescription());
        street.setText(report.getStreet());
        houseNumber.setText(report.getHouseNumber() + "");
        zipCode.setText(report.getZipCode() + "");
        city.setText(report.getCity());
        modifyReportButton.setText(R.string.modify);

        modifyReportButton.setOnClickListener(v -> {
            if(user != null) {
                checkData();

                if(modifyReportButton.isEnabled()) {
                    String descriptionText = description.getText().toString();
                    String streetText = street.getText().toString();
                    String cityText = city.getText().toString();
                    Integer houseNumberInteger = Integer.parseInt(houseNumber.getText().toString());
                    Integer zipCodeInteger = Integer.parseInt(zipCode.getText().toString());

                    report.setDescription(descriptionText);
                    report.setCity(cityText);
                    report.setStreet(streetText);
                    report.setHouseNumber(houseNumberInteger);
                    report.setZipCode(zipCodeInteger);

                    modifyReportButton.setEnabled(false);
                    host.modifyReport(report);
                    ReportModifyDialog.this.getDialog().cancel();
                }
            } else {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.login_connexion, R.string.asking_connection_report);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReportModifyDialog.this.getDialog().cancel();
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
                if(!modifyReportButton.isEnabled())
                    checkData();
            }
        };

        description.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        houseNumber.addTextChangedListener(textWatcher);
        zipCode.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);

        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.TOKEN, null);

        if(token != null) {
            accountViewModel.getUserFromToken(token);
        }

        accountViewModel.getUser().observe(this, user -> {
            this.user = user;
        });

        reportViewModel.getInputErrors().observe(this, inputErrors -> {
            if(!inputErrors.isEmpty()) {
                description.setError(inputErrors.containsKey("description") ? inputErrors.get("description") : null);
                street.setError(inputErrors.containsKey("street") ? inputErrors.get("street") : null);
                houseNumber.setError(inputErrors.containsKey("houseNumber") ? inputErrors.get("houseNumber") : null);
                zipCode.setError(inputErrors.containsKey("zipCode") ? inputErrors.get("zipCode") : null);
                city.setError(inputErrors.containsKey("city") ? inputErrors.get("city") : null);
            }
            modifyReportButton.setEnabled(inputErrors.isEmpty());
        });
    }

    public static ReportModifyDialog getInstance() {
        if(instance == null)
            instance = new ReportModifyDialog();
        return instance;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void checkData() {
        reportViewModel.checkData(
                description.getText().toString(),
                street.getText().toString(),
                houseNumber.getText().toString(),
                zipCode.getText().toString(),
                city.getText().toString()
        );
    }
}
