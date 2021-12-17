package com.example.smartcity_app.view.fragment;

import static com.example.smartcity_app.util.Constants.MAPVIEW_BUNDLE_KEY;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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
import com.example.smartcity_app.util.CallbackParticipationModification;
import com.example.smartcity_app.view.MainActivity;
import com.example.smartcity_app.view.dialog.EventCreationDialog;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.view.recyclerView.EventRecyclerView.EventAdapter;
import com.example.smartcity_app.util.CallbackEventCreation;
import com.example.smartcity_app.viewModel.EventViewModel;
import com.example.smartcity_app.viewModel.ParticipationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

public class ReportFragment extends Fragment implements CallbackEventCreation, CallbackParticipationModification, OnMapReadyCallback {
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
    private ParticipationViewModel participationViewModel;
    private Report report;
    private MapView mapView;

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
        mapView = (MapView) root.findViewById(R.id.mapview);

        initGoogleMap(savedInstanceState);

        report = (Report) getArguments().getSerializable("touchedReport");
        String addressValue = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(report.getCreationDate());
        String dateString = date.get(GregorianCalendar.DAY_OF_MONTH) + "/" + date.get(GregorianCalendar.MONTH) + "/" + date.get(GregorianCalendar.YEAR);

        locationTextView.setText(report.getCity());
        dateTextView.setText(dateString);
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

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if(savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        participationViewModel = new ViewModelProvider(this).get(ParticipationViewModel.class);

        EventAdapter eventAdapter = new EventAdapter(eventViewModel.getEvents().getValue(), this);

        eventViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
             eventAdapter.setEvents(events);
        });

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
        event.setCreatorId(MainActivity.getUser().getId());

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
            Address location = addresses.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
}