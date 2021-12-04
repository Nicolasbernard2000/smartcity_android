package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.ui.MainActivity;
import com.example.smartcity_app.ui.dialog.InformationDialog;
import com.example.smartcity_app.ui.recyclerView.ReportRecyclerView;
import com.example.smartcity_app.viewModels.ReportViewModel;

public class ProfilePersonalReportsFragment extends Fragment {
    private ReportViewModel reportViewModel;

    public ProfilePersonalReportsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_personal_reports_fragment, container, false);

        RecyclerView reportsRecyclerView = root.findViewById(R.id.personal_report_recycler_view);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        ReportRecyclerView.ReportAdapter reportAdapter = new ReportRecyclerView.ReportAdapter(container, reportViewModel.getReports().getValue());
        reportViewModel.getReports().observe(getViewLifecycleOwner(), reportAdapter::setReports);
        reportsRecyclerView.setAdapter(reportAdapter);
        reportViewModel.getReportsFromWebWithUserId(MainActivity.getUser().getId());

        reportViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });

        return root;
    }
}
