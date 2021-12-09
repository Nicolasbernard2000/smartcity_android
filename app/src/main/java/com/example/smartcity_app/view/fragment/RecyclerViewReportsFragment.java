package com.example.smartcity_app.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.view.dialog.InformationDialog;
import com.example.smartcity_app.view.recyclerView.ReportRecyclerView.ReportAdapter;
import com.example.smartcity_app.viewModel.ReportViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecyclerViewReportsFragment extends Fragment {
    private RecyclerView reportsRecyclerView;
    private ReportAdapter reportAdapter;
    private List<Report> reportsList;
    private EditText research;
    private ReportViewModel reportViewModel;

    public RecyclerViewReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view_reports_fragment, container, false);

        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        reportsRecyclerView = root.findViewById(R.id.report_recycler_view);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        reportAdapter = new ReportAdapter(container, reportViewModel.getReports().getValue());
        reportsRecyclerView.setAdapter(reportAdapter);

        research = root.findViewById(R.id.report_research);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable editable) {
                String researchText = research.getText().toString();
                if(researchText.isEmpty()) {
                    reportAdapter.setReports(reportsList);
                } else {
                    List<Report> reportsFromResearch = reportsList.stream().filter(report -> report.getCity().toLowerCase(Locale.ROOT).contains(researchText)).collect(Collectors.toList());
                    reportAdapter.setReports(reportsFromResearch);
                }
            }
        };

        research.addTextChangedListener(textWatcher);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reportViewModel.getReportsFromWeb();
        reportViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            reportsList = reports;
            reportAdapter.setReports(reportsList);
        });

        reportViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });
    }
}