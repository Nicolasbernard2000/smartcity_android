package com.example.smartcity_app.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Participation;
import com.example.smartcity_app.util.CallbackParticipationModification;
import com.example.smartcity_app.viewModel.ParticipationViewModel;

public class ParticipationDialog extends DialogFragment {
    private RadioButton participateRadioButton;
    private RadioButton notParticipateRadioButton;
    private static ParticipationDialog instance;
    private CallbackParticipationModification host;
    private int userID;
    private int eventID;
    private boolean wasAlreadyRegistered;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        host = (CallbackParticipationModification) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.participation_dialog, null);

        participateRadioButton = (RadioButton) view.findViewById(R.id.radio_button_participate);
        notParticipateRadioButton = (RadioButton) view.findViewById(R.id.radio_button_dont_participate);
        userID = getArguments().getInt("userID");
        eventID = getArguments().getInt("eventID");

        builder.setView(view)
                .setPositiveButton(R.string.im_certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Participation participation = null;
                        // Si l'user indique qui participera et qu'il n'était pas enregistré
                        if(participateRadioButton.isChecked() && !wasAlreadyRegistered) {
                            participation = new Participation(userID, eventID);
                        } else {
                            // Si l'user indique qui participera pas et qu'il était enregistré
                            if(notParticipateRadioButton.isChecked() && wasAlreadyRegistered) {
                                participation = new Participation(userID, eventID);
                            }
                        }
                        host.actionParticipation(participation, wasAlreadyRegistered);
                        ParticipationDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParticipationDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParticipationViewModel participationViewModel = new ViewModelProvider(this).get(ParticipationViewModel.class);
        participationViewModel.getParticipationFromWeb(userID, eventID);
        participationViewModel.getParticipation().observe(this, participation -> {
            if(participation.isUserRegisteredForEvent()) {
                participateRadioButton.setChecked(true);
                wasAlreadyRegistered = true;
            } else {
                notParticipateRadioButton.setChecked(true);
                wasAlreadyRegistered = false;
            }
        });

        participationViewModel.getError().observe(this, error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });
    }

    public static ParticipationDialog getInstance() {
        if(instance == null)
            instance = new ParticipationDialog();
        return instance;
    }
}
