package com.example.smartcity_app.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.view.MainActivity;
import com.example.smartcity_app.view.dialog.EventCreationDialog;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.view.recyclerView.EventRecyclerView.EventAdapter;
import com.example.smartcity_app.util.CallbackEventCreation;
import com.example.smartcity_app.viewModel.EventViewModel;

public class ReportFragment extends Fragment implements CallbackEventCreation {
    private TextView locationTextView;
    private TextView dateTextView;
    private TextView typeTextView;
    private TextView addressTextView;
    private TextView statusTextView;
    private TextView idTextView;
    private TextView descriptionTextView;
    private Button createEventButton;
    private RecyclerView eventsRecyclerView;
    private EventViewModel eventViewModel;
    private Report report;

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

        locationTextView = root.findViewById(R.id.report_location);
        dateTextView = root.findViewById(R.id.report_date);
        typeTextView = root.findViewById(R.id.report_type);
        addressTextView = root.findViewById(R.id.report_address);
        statusTextView = root.findViewById(R.id.report_status);
        idTextView = root.findViewById(R.id.report_id);
        descriptionTextView = root.findViewById(R.id.report_description);
        createEventButton = root.findViewById(R.id.create_event_button);
        eventsRecyclerView = root.findViewById(R.id.event_recycler_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        report = (Report) getArguments().getSerializable("touchedReport");
        String addressValue = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();

        locationTextView.setText(report.getCity());
        dateTextView.setText(report.getCreationDate().toString());
        typeTextView.setText(report.getReportType().getLabel());
        addressTextView.setText(addressValue);
        statusTextView.setText(getString(getResources().getIdentifier("state_" + report.getState(), "string", getContext().getPackageName())));
        idTextView.setText("#" + report.getId().toString());
        descriptionTextView.setText(report.getDescription());
        createEventButton.setOnClickListener(v -> {
            if(!MainActivity.isUserConnected()) {
                InformationDialog test = InformationDialog.getInstance();
                test.setInformation(R.string.login_connexion, R.string.asking_connection_event);
                test.show(getParentFragmentManager().beginTransaction(), null);
            } else {
                EventCreationDialog eventCreationFragment = new EventCreationDialog();
                eventCreationFragment.setTargetFragment(this, 0);
                eventCreationFragment.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        EventAdapter eventAdapter = new EventAdapter(eventViewModel.getEvents().getValue());
        eventViewModel.getEvents().observe(getViewLifecycleOwner(), eventAdapter::setEvents);
        eventsRecyclerView.setAdapter(eventAdapter);
        eventViewModel.getEventsFromWebWithReportId(report.getId());

        eventViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
            int typeMessage;
            int message;
            switch(code) {
                case 200:
                    typeMessage = R.string.success;
                    message = R.string.event_created;
                    eventViewModel.getEventsFromWebWithReportId(report.getId());
                    break;
                case 404:
                    typeMessage = R.string.error;
                    message = R.string.error_id;
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
        });

        eventViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });
    }

    @Override
    public void createEvent(Event event) {
        event.setReportId(report.getId());
        event.setCreatorId(MainActivity.getUser().getId());

        eventViewModel.postEventOnWeb(event);
    }
}