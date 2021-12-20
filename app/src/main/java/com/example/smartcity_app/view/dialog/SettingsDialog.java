package com.example.smartcity_app.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.smartcity_app.R;
import com.example.smartcity_app.view.MainActivity;

import java.util.Locale;

public class SettingsDialog extends DialogFragment {
    private static SettingsDialog instance;
    private static MainActivity mainActivity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_fragment, null);
        ImageButton frButton = (ImageButton) view.findViewById(R.id.french_button);
        ImageButton enButton = (ImageButton) view.findViewById(R.id.english_button);

        builder.setView(view)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SettingsDialog.this.getDialog().cancel();
                    }
                });

        frButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("fr");
                mainActivity.refresh();
            }
        });

        enButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("en");
                mainActivity.refresh();
            }
        });

        return builder.create();
    }

    public static SettingsDialog getInstance(MainActivity mainActivity) {
        SettingsDialog.mainActivity = mainActivity;
        if(instance == null)
            instance = new SettingsDialog();
        return instance;
    }

    private void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
