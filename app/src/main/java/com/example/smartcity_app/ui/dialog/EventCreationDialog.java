package com.example.smartcity_app.ui.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.utils.CallbackEventCreation;

import java.util.Date;
import java.util.GregorianCalendar;

public class EventCreationDialog extends DialogFragment {
    private EditText date;
    private EditText hour;
    private EditText duration;
    private EditText description;
    private CallbackEventCreation host;
    private GregorianCalendar dateHour = new GregorianCalendar();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        host = (CallbackEventCreation) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.event_creation_fragment, null);
        setupViews(view);

        builder.setView(view)
                .setPositiveButton(R.string.create_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO check des valeurs
                        if(MainActivity.getUser() == null) {
                            Toast.makeText(getContext(), getText(R.string.asking_connection_report), Toast.LENGTH_LONG).show();
                        } else {
                            Date dateValue = new Date(dateHour.getTimeInMillis());
                            Integer durationValue = Integer.parseInt(duration.getText().toString());
                            String descriptionValue = description.getText().toString();
                            Event event = new Event(dateValue, durationValue, descriptionValue, null, null);
                            host.createEvent(event);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EventCreationDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void setupViews(View root) {
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
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month) + "/" + year);
                dateHour.set(GregorianCalendar.YEAR, year);
                dateHour.set(GregorianCalendar.MONTH, month);
                dateHour.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);
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
                    dateHour.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
                    dateHour.set(GregorianCalendar.MINUTE, minute);
                }
            };

            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeSetListener, 0, 0, true);
                timePickerDialog.show();
            }
        });
    }
}