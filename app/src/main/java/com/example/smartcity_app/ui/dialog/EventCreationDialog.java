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
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.smartcity_app.viewModels.EventViewModel;

import java.util.Date;
import java.util.GregorianCalendar;

public class EventCreationDialog extends DialogFragment {
    private EditText date;
    private EditText hour;
    private EditText duration;
    private EditText description;
    private CallbackEventCreation host;
    private GregorianCalendar dateHour = new GregorianCalendar();
    private EventViewModel eventViewModel;
    private boolean areDataGood = true;

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
        View root = inflater.inflate(R.layout.event_creation_fragment, null);

        date = (EditText) root.findViewById(R.id.create_event_date);
        hour = (EditText) root.findViewById(R.id.create_event_hour);
        duration = (EditText) root.findViewById(R.id.create_event_duration);
        description = (EditText) root.findViewById(R.id.create_event_description);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!areDataGood) {
                    checkData();
                }
            }
        };

        date.addTextChangedListener(textWatcher);
        hour.addTextChangedListener(textWatcher);
        duration.addTextChangedListener(textWatcher);
        description.addTextChangedListener(textWatcher);

        GregorianCalendar today = new GregorianCalendar();
        int year = today.get(GregorianCalendar.YEAR);
        int month = today.get(GregorianCalendar.MONTH);
        int day = today.get(GregorianCalendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(String.format("%02d", dayOfMonth) + "/" + String.format("%02d", month + 1) + "/" + year);
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

        builder.setView(root)
                .setPositiveButton(R.string.create_event, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EventCreationDialog.this.getDialog().dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog alertDialog = (AlertDialog)getDialog();
        if(alertDialog != null) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkData();
                    if(areDataGood) {
                        Date dateValue = new Date(dateHour.getTimeInMillis());
                        Integer durationValue = Integer.parseInt(duration.getText().toString());
                        String descriptionValue = description.getText().toString();
                        Event event = new Event(dateValue, durationValue, descriptionValue, null, null);
                        host.createEvent(event);
                        dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        eventViewModel.getInputErrors().observe(getParentFragment().getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                date.setError(inputErrors.containsKey("date") ? inputErrors.get("date") : null);
                hour.setError(inputErrors.containsKey("hour") ? inputErrors.get("hour") : null);
                duration.setError(inputErrors.containsKey("duration") ? inputErrors.get("duration") : null);
                description.setError(inputErrors.containsKey("description") ? inputErrors.get("description") : null);
            }
            areDataGood = inputErrors.isEmpty();
        });
    }

    public void checkData() {
        eventViewModel.checkData(
                date.getText().toString(),
                hour.getText().toString(),
                duration.getText().toString(),
                description.getText().toString()
        );
    }
}