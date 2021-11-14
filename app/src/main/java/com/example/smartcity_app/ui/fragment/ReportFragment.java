package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;

public class ReportFragment extends Fragment {
    private Report report;
    private TextView location;
    private TextView date;
    private TextView type;
    private TextView address;
    private TextView status;
    private TextView id;
    private TextView description;

    public ReportFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_fragment, container, false);

        location = root.findViewById(R.id.report_location);
        date = root.findViewById(R.id.report_date);
        type = root.findViewById(R.id.report_type);
        address = root.findViewById(R.id.report_address);
        status = root.findViewById(R.id.report_status);
        id = root.findViewById(R.id.report_id);
        description = root.findViewById(R.id.report_description);

        if (getArguments() != null) {
            report = (Report) getArguments().getSerializable("touchedReport");

            String address = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();

            location.setText(report.getCity());
            date.setText(report.getCreationDate());
            type.setText(report.getType());
            this.address.setText(address);
            status.setText(report.getStatus());
            id.setText(report.getId() + "");
            description.setText(report.getDescription());

        } else {
            //TODO : afficher une page spécifique avec un message erreur
        }

        return root;
    }
}