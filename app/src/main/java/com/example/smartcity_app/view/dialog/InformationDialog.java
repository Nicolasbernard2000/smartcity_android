package com.example.smartcity_app.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.smartcity_app.R;

public class InformationDialog extends DialogFragment {
    private TextView informationTypeTextView;
    private TextView informationTextView;
    private Integer informationTypeKey;
    private Integer informationKey;
    private static InformationDialog instance;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.information_dialog, null);
        setupViews(view);

        informationTypeTextView.setText(getContext().getResources().getText(informationTypeKey));
        informationTextView.setText(getContext().getResources().getText(informationKey));

        builder.setView(view)
                .setPositiveButton(R.string.understand, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InformationDialog.this.getDialog().cancel();

                    }
                });

        return builder.create();
    }

    private void setupViews(View root) {
        informationTypeTextView = (TextView)root.findViewById(R.id.information_type);
        informationTextView = (TextView)root.findViewById(R.id.information);
    }

    public void setInformation(int informationTypeKey, Integer informationKey) {
        this.informationTypeKey = informationTypeKey;
        this.informationKey = informationKey;
    }

    public static InformationDialog getInstance() {
        if(instance == null)
            instance = new InformationDialog();
        return instance;
    }
}
