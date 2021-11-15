package com.example.smartcity_app.ui.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.smartcity_app.R;

import java.util.GregorianCalendar;

public class EventCreationFragment extends Fragment {
    private EditText date;
    private EditText hour;
    private EditText duration;
    private EditText description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_creation_fragment, container, false);

        date = (EditText) root.findViewById(R.id.create_event_date);
        hour = (EditText) root.findViewById(R.id.create_event_hour);
        duration = (EditText) root.findViewById(R.id.create_event_duration);
        description = (EditText) root.findViewById(R.id.create_event_description);

        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(GregorianCalendar.YEAR);
        int month = today.get(GregorianCalendar.MONTH);
        int day = today.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", monthOfYear) + "/" + year);
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        hour.setOnClickListener(new View.OnClickListener() {
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                }
            };

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeSetListener, 0, 0, true);
                timePickerDialog.show();
            }
        });

        return root;
    }
}