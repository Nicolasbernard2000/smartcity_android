package com.example.smartcity_app.view.fragment;

import static com.example.smartcity_app.util.Constants.MAPVIEW_BUNDLE_KEY;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.smartcity_app.model.Participation;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.util.CallbackEventDelete;
import com.example.smartcity_app.util.CallbackEventModify;
import com.example.smartcity_app.util.CallbackParticipationModification;
import com.example.smartcity_app.util.Constants;
import com.example.smartcity_app.view.dialog.EventOperationDialog;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.view.recyclerView.EventRecyclerView.EventAdapter;
import com.example.smartcity_app.util.CallbackEventCreation;
import com.example.smartcity_app.viewModel.AccountViewModel;
import com.example.smartcity_app.viewModel.EventViewModel;
import com.example.smartcity_app.viewModel.ParticipationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

public class ReportFragment extends Fragment implements CallbackEventCreation, CallbackParticipationModification, CallbackEventDelete, CallbackEventModify, OnMapReadyCallback {
    private TextView locationTextView, dateTextView, typeTextView, addressTextView, statusTextView, idTextView, descriptionTextView;
    private Button createEventButton;
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private EventViewModel eventViewModel;
    private AccountViewModel accountViewModel;
    private ParticipationViewModel participationViewModel;
    private Report report;
    private MapView mapView;
    private User user;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_fragment, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

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
        eventAdapter = new EventAdapter(this);
        eventsRecyclerView.setAdapter(eventAdapter);
        mapView = root.findViewById(R.id.mapview);

        initGoogleMap(savedInstanceState);

        report = (Report) getArguments().getSerializable("touchedReport");
        String addressValue = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(report.getCreationDate());
        int day = date.get(GregorianCalendar.DAY_OF_MONTH);
        int month = date.get(GregorianCalendar.MONTH) + 1;
        int year = date.get(GregorianCalendar.YEAR);
        String dateString = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year;

        locationTextView.setText(report.getCity());
        dateTextView.setText(dateString);
        typeTextView.setText(report.getReportType().getLabel());
        addressTextView.setText(addressValue);
        statusTextView.setText(getString(getResources().getIdentifier("state_" + report.getState(), "string", getContext().getPackageName())));
        idTextView.setText("#" + report.getId().toString());
        descriptionTextView.setText(report.getDescription());
        createEventButton.setOnClickListener(v -> {
            if(token == null) {
                InformationDialog test = InformationDialog.getInstance();
                test.setInformation(R.string.login_connexion, R.string.asking_connection_event);
                test.show(getParentFragmentManager().beginTransaction(), null);
            } else {
                EventOperationDialog eventOperationDialog = new EventOperationDialog(null);
                eventOperationDialog.setTargetFragment(this, 0);
                eventOperationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        participationViewModel = new ViewModelProvider(this).get(ParticipationViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
            eventAdapter.setUser(user);
        });

        eventViewModel.getEvents().observe(getViewLifecycleOwner(), eventAdapter::setEvents);
        eventViewModel.getEventsFromWebWithReportId(report.getId());

        if(token != null) {
            accountViewModel.getUserFromToken(token);
        }


        eventViewModel.getStatusCodeCreation().observe(getViewLifecycleOwner(), code -> {
            int typeMessage;
            int message;
            switch(code) {
                case 201:
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

        eventViewModel.getStatusCodeDelete().observe(getViewLifecycleOwner(), code -> {
            Integer typeMessage;
            Integer message;
            switch(code) {
                case 204:
                    typeMessage = R.string.success;
                    message = R.string.event_deleted;
                    eventViewModel.getEventsFromWebWithReportId(report.getId());
                    break;
                case 400:
                    typeMessage = R.string.error;
                    message = R.string.error_request;
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
        });

        eventViewModel.getStatusCodePatch().observe(getViewLifecycleOwner(), code -> {
            Integer typeMessage;
            Integer message;
            switch(code) {
                case 204:
                    typeMessage = R.string.success;
                    message = R.string.report_modified;
                    eventViewModel.getEventsFromWebWithReportId(report.getId());
                    break;
                case 400:
                    typeMessage = R.string.error;
                    message = R.string.error_request;
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
        });

        eventViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });

        participationViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
            if(code == 500) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, R.string.error_id);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        participationViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });
    }

    @Override
    public void createEvent(Event event) {
        event.setReportId(report.getId());
        event.setCreatorId(user.getId());

        eventViewModel.postEventOnWeb(event);
    }

    @Override
    public void actionParticipation(Participation participation, boolean wasAlreadyRegistered) {
        if(participation == null)
            return;

        if(wasAlreadyRegistered) {
            participationViewModel.deleteParticipationOnWeb(participation);
        } else {
            participationViewModel.postParticipationOnWeb(participation);
        }
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if(savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(report.getAddress(), 5);
            if(!addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng placeReport = new LatLng(location.getLatitude(), location.getLongitude());
                map.addMarker(new MarkerOptions().position(placeReport).title("Marker"));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(placeReport)
                        .zoom(15)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void deleteEvent(Event event) {
        eventViewModel.deleteEventOnWeb(event);
    }

    @Override
    public void modifyEvent(Event event) {
        eventViewModel.modifyEventOnWeb(event);
    }
}