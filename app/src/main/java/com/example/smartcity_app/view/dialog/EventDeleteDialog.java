package com.example.smartcity_app.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.util.CallbackEventDelete;

public class EventDeleteDialog extends DialogFragment {
    private TextView informationTypeTextView;
    private TextView informationTextView;
    private static EventDeleteDialog instance;
    private CallbackEventDelete host;
    private Event event;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.information_dialog, null);

        informationTypeTextView = view.findViewById(R.id.information_type);
        informationTextView = view.findViewById(R.id.information);
        informationTypeTextView.setText(getContext().getResources().getText(R.string.delete));
        informationTextView.setText(getContext().getResources().getText(R.string.delete_event));

        builder.setView(view)
                .setPositiveButton(R.string.im_certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        host.deleteEvent(event);
                        EventDeleteDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EventDeleteDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        host = (CallbackEventDelete) getTargetFragment();
    }

    public static EventDeleteDialog getInstance() {
        if(instance == null)
            instance = new EventDeleteDialog();
        return instance;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
