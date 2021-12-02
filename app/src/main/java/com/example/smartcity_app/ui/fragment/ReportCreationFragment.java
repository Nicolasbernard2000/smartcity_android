package com.example.smartcity_app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.ui.dialog.InformationDialog;
import com.example.smartcity_app.viewModels.ReportTypeViewModel;
import com.example.smartcity_app.viewModels.ReportViewModel;

import java.util.ArrayList;

public class ReportCreationFragment extends Fragment {
    private Button createReportButton;
    private ReportViewModel viewModelReport;
    private ReportTypeViewModel viewModelReportType;
    private Spinner reportType;
    private TextView description;
    private TextView street;
    private TextView houseNumber;
    private TextView zipCode;
    private TextView city;
    private Context context;
    private ArrayList<ReportType> reportTypes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_creation_fragment, container, false);
        this.context = getContext();

        setupViews(root);

        viewModelReportType = new ViewModelProvider(this).get(ReportTypeViewModel.class);
        viewModelReport = new ViewModelProvider(this).get(ReportViewModel.class);

        viewModelReportType.getReportTypesFromWeb();
        viewModelReportType.getReportTypes().observe(getViewLifecycleOwner(), ReportTypes -> {
            this.reportTypes = (ArrayList<ReportType>) ReportTypes;
            ArrayList<String> labels = new ArrayList<>();
            for (ReportType reportType: ReportTypes) {
                labels.add(reportType.getLabel());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, labels);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            reportType.setAdapter(adapter);
        });

        return root;
    }

    private void setupViews(View root) {
        reportType = (Spinner) root.findViewById(R.id.create_report_report_type);
        description = (TextView) root.findViewById(R.id.create_report_description);
        street = (TextView) root.findViewById(R.id.create_report_street);
        houseNumber = (TextView) root.findViewById(R.id.create_report_house_number);
        zipCode = (TextView) root.findViewById(R.id.create_report_zip_code);
        city = (TextView) root.findViewById(R.id.create_report_city);
        createReportButton = (Button) root.findViewById(R.id.create_report_button);
        createReportButton.setOnClickListener(new CreateReportListener());
    }

    private class CreateReportListener implements View.OnClickListener {
        public CreateReportListener() {}
        @Override
        public void onClick(View v) {
            InformationDialog informationDialog = InformationDialog.getInstance();
            if(MainActivity.isUserConnected()) {
                int selectedItemPosition = reportType.getSelectedItemPosition();
                ReportType reportType = reportTypes.get(selectedItemPosition);
                String descriptionText = description.getText().toString();
                String streetText = street.getText().toString();
                String cityText = city.getText().toString();
                try {
                    Integer houseNumberInteger = Integer.parseInt(houseNumber.getText().toString());
                    Integer zipCodeInteger = Integer.parseInt(zipCode.getText().toString());
                    viewModelReport.postReportOnWeb(new Report(descriptionText, "pending", cityText, streetText, zipCodeInteger, houseNumberInteger, MainActivity.getUser().getId(), reportType));
                    viewModelReport.getStatusCode().observe(getViewLifecycleOwner(), code -> {
                        Integer idType;
                        Integer idMessage;
                        switch(code) {
                            case 200:
                                idType = R.string.success;
                                idMessage = R.string.report_created;
                                break;
                            case 404:
                                idType = R.string.error;
                                idMessage = R.string.wrong_datas;
                                break;
                            case 500:
                                idType = R.string.error;
                                idMessage = R.string.error_servor;
                                break;
                            default:
                                idType = R.string.error;
                                idMessage = R.string.error_unknown;
                        }
                        informationDialog.setInformation(idType, idMessage);
                    });
                } catch (Exception e) {
                    Toast.makeText(context, R.string.number_only, Toast.LENGTH_LONG).show();
                }
            } else {
                informationDialog.setInformation(R.string.login_connexion, R.string.asking_connection_report);
            }
            informationDialog.show(getParentFragmentManager(), null);
        }
    }
}
