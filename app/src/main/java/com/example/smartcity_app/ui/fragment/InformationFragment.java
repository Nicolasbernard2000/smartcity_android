package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smartcity_app.R;

public class InformationFragment extends Fragment {
    private String information;
    private TextView informationTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.information_fragment, container, false);
        informationTextView = root.findViewById(R.id.information_description);

        if (getArguments() != null) {
            information = (String) getArguments().getString("information");
            informationTextView.setText(information);
        }

        return root;
    }
}
