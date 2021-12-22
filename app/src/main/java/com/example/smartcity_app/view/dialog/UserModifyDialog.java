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
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.util.CallbackUserModify;

public class UserModifyDialog extends DialogFragment {
    private TextView informationTypeTextView;
    private TextView informationTextView;
    private static UserModifyDialog instance;
    private CallbackUserModify host;
    private User user;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        host = (CallbackUserModify) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.information_dialog, null);

        informationTypeTextView = view.findViewById(R.id.information_type);
        informationTextView = view.findViewById(R.id.information);
        informationTypeTextView.setText(getContext().getResources().getText(R.string.modify));
        informationTextView.setText(getContext().getResources().getText(R.string.modify_account_question));

        builder.setView(view)
                .setPositiveButton(R.string.im_certain, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        host.modifyUser(user);
                        UserModifyDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserModifyDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public static UserModifyDialog getInstance() {
        if(instance == null)
            instance = new UserModifyDialog();
        return instance;
    }

    public void setUser(User user){
        this.user = user;
    }
}
