package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smartcity_app.R;
import com.example.smartcity_app.ui.MainActivity;

public class ReportCreationFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_creation_fragment, container, false);

        if(!MainActivity.isUserConnected()) {
            Log.d("Debug", "User pas connecté, affichage autre page");

            Bundle bundle = new Bundle();
            bundle.putString("information", getString(R.string.asking_connection));
            Navigation.findNavController(container).navigate(R.id.action_fragment_add_report_to_fragment_information, bundle);
        }

        return view;
    }
}
