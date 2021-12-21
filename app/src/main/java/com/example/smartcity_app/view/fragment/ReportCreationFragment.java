package com.example.smartcity_app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.view.MainActivity;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.viewModel.ReportTypeViewModel;
import com.example.smartcity_app.viewModel.ReportViewModel;

import java.util.ArrayList;

public class ReportCreationFragment extends Fragment {
    private ReportViewModel reportViewModel;
    private ReportTypeViewModel reportTypeViewModel;
    private Spinner reportType;
    private TextView reportText;
    private TextView description;
    private TextView street;
    private TextView houseNumber;
    private TextView zipCode;
    private TextView city;
    private Button createReportButton;
    private Context context;
    private ArrayList<ReportType> reportTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_operation_fragment, container, false);
        this.context = getContext();

        reportText = root.findViewById(R.id.text_report);
        reportType = root.findViewById(R.id.create_report_report_type);
        description = root.findViewById(R.id.create_report_description);
        street = root.findViewById(R.id.create_report_street);
        houseNumber = root.findViewById(R.id.create_report_house_number);
        zipCode = root.findViewById(R.id.create_report_zip_code);
        city = root.findViewById(R.id.create_report_city);
        createReportButton = root.findViewById(R.id.report_button);

        reportText.setText(R.string.create_report);
        createReportButton.setText(R.string.create_report_button);

        createReportButton.setOnClickListener(v -> {
            if(MainActivity.isUserConnected()) {
                checkData();

                if(createReportButton.isEnabled()) {
                    int selectedItemPosition = reportType.getSelectedItemPosition();
                    ReportType reportType = reportTypes.get(selectedItemPosition);
                    String descriptionText = description.getText().toString();
                    String streetText = street.getText().toString();
                    String cityText = city.getText().toString();
                    Integer houseNumberInteger = Integer.parseInt(houseNumber.getText().toString());
                    Integer zipCodeInteger = Integer.parseInt(zipCode.getText().toString());

                    Report report = new Report(
                            descriptionText,
                            Report.DEFAULT_STATE,
                            cityText,
                            streetText,
                            zipCodeInteger,
                            houseNumberInteger,
                            MainActivity.getUser(),
                            reportType
                    );
                    createReportButton.setEnabled(false);
                    reportViewModel.postReportOnWeb(report);
                }
            } else {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.login_connexion, R.string.asking_connection_report);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
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
                if(!createReportButton.isEnabled())
                    checkData();
            }
        };

        description.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        houseNumber.addTextChangedListener(textWatcher);
        zipCode.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        reportTypeViewModel = new ViewModelProvider(this).get(ReportTypeViewModel.class);

        reportTypeViewModel.getReportTypesFromWeb();
        reportTypeViewModel.getReportTypes().observe(getViewLifecycleOwner(), ReportTypes -> {
            this.reportTypes = (ArrayList<ReportType>) ReportTypes;
            ArrayList<String> labels = new ArrayList<>();
            for (ReportType reportType: ReportTypes) {
                labels.add(reportType.getLabel());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, labels);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reportType.setAdapter(adapter);
        });

        reportTypeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });

        reportViewModel.getInputErrors().observe(getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                description.setError(inputErrors.containsKey("description") ? inputErrors.get("description") : null);
                street.setError(inputErrors.containsKey("street") ? inputErrors.get("street") : null);
                houseNumber.setError(inputErrors.containsKey("houseNumber") ? inputErrors.get("houseNumber") : null);
                zipCode.setError(inputErrors.containsKey("zipCode") ? inputErrors.get("zipCode") : null);
                city.setError(inputErrors.containsKey("city") ? inputErrors.get("city") : null);
            }
            createReportButton.setEnabled(inputErrors.isEmpty());
        });

        reportViewModel.getStatusCodeCreation().observe(getViewLifecycleOwner(), code -> {
            Integer typeMessage;
            Integer message;
            switch(code) {
                case 201:
                    typeMessage = R.string.success;
                    message = R.string.report_created;
                    break;
                case 404:
                    typeMessage = R.string.error;
                    message = R.string.wrong_datas;
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
            createReportButton.setEnabled(true);
        });

        reportViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            createReportButton.setEnabled(true);
        });
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
