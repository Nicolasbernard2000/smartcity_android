package com.example.smartcity_app.view.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.util.CallbackReportDelete;
import com.example.smartcity_app.view.MainActivity;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.view.recyclerView.PersonalReportRecyclerView.PersonalReportAdapter;
import com.example.smartcity_app.viewModel.ReportViewModel;

public class ProfilePersonalReportsFragment extends Fragment implements CallbackReportDelete {
    private ReportViewModel reportViewModel;
    private RecyclerView reportsRecyclerView;
    private PersonalReportAdapter personalReportAdapter;

    public ProfilePersonalReportsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_personal_reports_fragment, container, false);

        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        reportsRecyclerView = root.findViewById(R.id.personal_report_recycler_view);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        personalReportAdapter = new PersonalReportAdapter(container, reportViewModel.getReports().getValue(), this);
        reportsRecyclerView.setAdapter(personalReportAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reportViewModel.getReportsFromWebWithUserId(MainActivity.getUser().getId());
        reportViewModel.getReports().observe(getViewLifecycleOwner(), personalReportAdapter::setReports);

        reportViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });
    }

    @Override
    public void deleteReport(Report report) {
        //TODO
        //reportViewModel.delete(report.getId());
        Log.i("Debug", report.getId() + " will be delete");
    }
}
