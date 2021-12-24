package com.example.smartcity_app.view.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.util.CallbackEventCreation;
import com.example.smartcity_app.util.CallbackEventModify;
import com.example.smartcity_app.viewModel.EventViewModel;

import java.util.Date;
import java.util.GregorianCalendar;

public class EventOperationDialog extends DialogFragment {
    private TextView eventText;
    private EditText date;
    private EditText hour;
    private EditText duration;
    private EditText description;
    private CallbackEventCreation hostCreation;
    private CallbackEventModify hostModification;
    private GregorianCalendar dateHour = new GregorianCalendar();
    private EventViewModel eventViewModel;
    private boolean areDataGood = true;
    private Event event;

    public EventOperationDialog(Event event) {
        this.event = event;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        hostCreation = (CallbackEventCreation) getTargetFragment();
        hostModification = (CallbackEventModify) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.event_operation_fragment, null);

        eventText = root.findViewById(R.id.text_event);
        date = root.findViewById(R.id.create_event_date);
        hour = root.findViewById(R.id.create_event_hour);
        duration = root.findViewById(R.id.create_event_duration);
        description = root.findViewById(R.id.create_event_description);

        if(event != null) {
            eventText.setText(R.string.event_modify);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(event.getDateHour());
            int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
            int month = calendar.get(GregorianCalendar.MONTH);
            int year = calendar.get(GregorianCalendar.YEAR);
            int hourOfDay = calendar.get(GregorianCalendar.HOUR_OF_DAY);
            int minute = calendar.get(GregorianCalendar.MINUTE);

            date.setText(String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/" + year);
            hour.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
            duration.setText(event.getDuration() + "");
            description.setText(event.getDescription());
        } else {
            eventText.setText(R.string.event_creation);
        }

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
                .setPositiveButton((event == null ? R.string.create_event : R.string.modify), null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EventOperationDialog.this.getDialog().dismiss();
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
                        String dateText = date.getText().toString();
                        String hourText = hour.getText().toString();
                        String[] tempDate = dateText.split("/");
                        String[] tempHour = hourText.split(":");
                        int day = Integer.parseInt(tempDate[0]);
                        int month = Integer.parseInt(tempDate[1]);
                        int year = Integer.parseInt(tempDate[2]);
                        int hour = Integer.parseInt(tempHour[0]);
                        int minute = Integer.parseInt(tempHour[1]);

                        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, day, hour + 1, minute);
                        Date dateValue = new Date(calendar.getTimeInMillis());
                        Integer durationValue = Integer.parseInt(duration.getText().toString());
                        String descriptionValue = description.getText().toString();

                        if(event == null) {
                            Event event = new Event(dateValue, durationValue, descriptionValue, null, null);
                            hostCreation.createEvent(event);
                        } else {
                            event.setDateHour(dateValue);
                            event.setDuration(durationValue);
                            event.setDescription(descriptionValue);
                            hostModification.modifyEvent(event);
                        }
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
                date.setError(inputErrors.containsKey("date") ? getString(inputErrors.get("date")) : null);
                hour.setError(inputErrors.containsKey("hour") ? getString(inputErrors.get("hour")) : null);
                duration.setError(inputErrors.containsKey("duration") ? getString(inputErrors.get("duration")) : null);
                description.setError(inputErrors.containsKey("description") ? getString(inputErrors.get("description")) : null);
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
