package com.example.smartcity_app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.viewModels.ReportTypeViewModel;
import com.example.smartcity_app.viewModels.ReportViewModel;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_creation_fragment, container, false);
        this.context = getContext();
        if(!MainActivity.isUserConnected()) {
            Log.d("Debug", "User pas connecté, affichage autre page");

            Bundle bundle = new Bundle();
            bundle.putString("information", getString(R.string.asking_connection));
            Navigation.findNavController(container).navigate(R.id.action_fragment_add_report_to_fragment_information, bundle);
        } else {
            viewModelReportType = new ViewModelProvider(this).get(ReportTypeViewModel.class);
            viewModelReport = new ViewModelProvider(this).get(ReportViewModel.class);

            reportType = (Spinner) root.findViewById(R.id.create_report_report_type);
            description = (TextView) root.findViewById(R.id.create_report_description);
            street = (TextView) root.findViewById(R.id.create_report_street);
            houseNumber = (TextView) root.findViewById(R.id.create_report_house_number);
            zipCode = (TextView) root.findViewById(R.id.create_report_zip_code);
            city = (TextView) root.findViewById(R.id.create_report_city);
            createReportButton = (Button) root.findViewById(R.id.create_report_button);
            createReportButton.setOnClickListener(new CreateReportListener());
/*
            viewModelReportType.getReportTypesFromWeb();
            viewModelReportType.getReportTypes().observe(getViewLifecycleOwner(), ReportType -> {
                ArrayAdapter<ReportType> adapter = new ArrayAdapter<ReportType>(getContext(), android.R.layout.simple_spinner_item, (List<ReportType>) viewModelReportType.getReportTypes());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reportType.setAdapter(adapter);
            });*/

        }

        return root;
    }

    private class CreateReportListener implements View.OnClickListener {
        public CreateReportListener() {}
        @Override
        public void onClick(View v) {
            //TODO vérifier les infos
            String descriptionText = description.getText().toString();
            String streetText = street.getText().toString();
            Integer houseNumberInteger = Integer.parseInt(houseNumber.getText().toString());
            Integer zipCodeInteger = Integer.parseInt(zipCode.getText().toString());
            String cityText = city.getText().toString();
            viewModelReport.postReportOnWeb(new Report(descriptionText, "En attente", cityText, streetText, zipCodeInteger, houseNumberInteger, new Date(), MainActivity.getUser().getId(), new ReportType(1, "label")));
            viewModelReport.getStatusCode().observe(getViewLifecycleOwner(), integer -> {
                //TODO message selon le code
                Toast.makeText(context, integer.toString(), Toast.LENGTH_LONG).show();
            });
        }
    }
}
