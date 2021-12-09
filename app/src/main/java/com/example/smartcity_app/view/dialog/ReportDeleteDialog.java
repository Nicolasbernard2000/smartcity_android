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
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.util.CallbackEventCreation;
import com.example.smartcity_app.util.CallbackReportDelete;

public class ReportDeleteDialog extends DialogFragment {
    private TextView informationTypeTextView;
    private TextView informationTextView;
    private static ReportDeleteDialog instance;
    private CallbackReportDelete host;
    private Report report;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        host = (CallbackReportDelete) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.information_dialog, null);
        setupViews(view);

        informationTypeTextView.setText(getContext().getResources().getText(R.string.delete));
        informationTextView.setText(getContext().getResources().getText(R.string.delete_report));

        builder.setView(view)
                .setPositiveButton(R.string.im_certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        host.deleteReport(report);
                        ReportDeleteDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ReportDeleteDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void setupViews(View root) {
        informationTypeTextView = (TextView)root.findViewById(R.id.information_type);
        informationTextView = (TextView)root.findViewById(R.id.information);
    }

    public static ReportDeleteDialog getInstance() {
        if(instance == null)
            instance = new ReportDeleteDialog();
        return instance;
    }

    public void setReport(Report report){
        this.report = report;
    }
}
