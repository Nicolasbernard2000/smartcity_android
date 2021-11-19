package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    private Button createEventButton;

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
        createEventButton = root.findViewById(R.id.create_event_button);

        if (getArguments() != null) {
            report = (Report) getArguments().getSerializable("touchedReport");

            String addressContent = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();

            location.setText(report.getCity());
            //date.setText(report.getCreationDate());
            type.setText(report.getReportType().getLabel());
            address.setText(addressContent);
            status.setText(report.getState());
            id.setText("ID");
            description.setText(report.getDescription());

            createEventButton.setOnClickListener(new CreateEventListener());
        } else {
            //TODO : afficher une page spécifique avec un message erreur
        }

        return root;
    }

    private class CreateEventListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Fragment childFragment = new EventCreationFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.test, childFragment).commit();

            view.setOnClickListener(new LeaveCreateEventListener(childFragment));
        }
    }

    private class LeaveCreateEventListener implements View.OnClickListener {
        private Fragment childFragment;

        public LeaveCreateEventListener(Fragment childFragment) {
            this.childFragment = childFragment;
        }

        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.detach(childFragment).commit();

            view.setOnClickListener(new CreateEventListener());
        }
    }
}