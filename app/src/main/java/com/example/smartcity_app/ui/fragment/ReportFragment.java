package com.example.smartcity_app.ui.fragment;

import android.content.Context;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.ui.recyclerView.EventRecyclerView.EventAdapter;
import com.example.smartcity_app.viewModels.EventViewModel;

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
    private RecyclerView eventsRecyclerView;
    private EventViewModel viewModel;
    private Context context;

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
        this.context = getContext();

        viewModel = new ViewModelProvider(this).get(EventViewModel.class);
        setupViews(root);

        if (getArguments() != null) {
            report = (Report) getArguments().getSerializable("touchedReport");

            String addressContent = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();

            location.setText(report.getCity());
            date.setText(report.getCreationDate().toString());
            type.setText(report.getReportType().getLabel());
            address.setText(addressContent);
            status.setText(context.getString(context.getResources().getIdentifier("state_" + report.getState(), "string", context.getPackageName())));
            id.setText("#" + report.getId().toString());
            description.setText(report.getDescription());
            createEventButton.setOnClickListener(new CreateEventListener());

            EventAdapter eventAdapter = new EventAdapter(viewModel.getEvents().getValue());
            viewModel.getEvents().observe(getViewLifecycleOwner(), eventAdapter::setEvents);
            eventsRecyclerView.setAdapter(eventAdapter);
            viewModel.getEventsFromWebWithReportId(report.getId());
        } else {
            //TODO : afficher une page spécifique avec un message erreur
        }

        return root;
    }

    private void setupViews(View root) {
        location = root.findViewById(R.id.report_location);
        date = root.findViewById(R.id.report_date);
        type = root.findViewById(R.id.report_type);
        address = root.findViewById(R.id.report_address);
        status = root.findViewById(R.id.report_status);
        id = root.findViewById(R.id.report_id);
        description = root.findViewById(R.id.report_description);
        createEventButton = root.findViewById(R.id.create_event_button);
        eventsRecyclerView = root.findViewById(R.id.event_recycler_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
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