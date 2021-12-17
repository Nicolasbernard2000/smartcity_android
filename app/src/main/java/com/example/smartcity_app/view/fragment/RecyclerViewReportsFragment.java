package com.example.smartcity_app.view.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecyclerViewReportsFragment extends Fragment {
    private RecyclerView reportsRecyclerView;
    private ReportAdapter reportAdapter;
    private ArrayList<Report> reportsList;
    private EditText research;
    private ReportViewModel reportViewModel;
    private ImageButton searchButton;
    private String searchText = "";
    private ProgressBar progressBar;
    private int numberReports = 0;
    private boolean isWaitingAnswer = false;

    public RecyclerViewReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view_reports_fragment, container, false);

        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        progressBar = root.findViewById(R.id.progress_bar);
        research = root.findViewById(R.id.report_research);
        searchButton = root.findViewById(R.id.search_button);
        reportsRecyclerView = root.findViewById(R.id.report_recycler_view);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        reportAdapter = new ReportAdapter(container, reportViewModel.getReports().getValue());
        reportsRecyclerView.setAdapter(reportAdapter);

        reportsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !isWaitingAnswer) {
                    isWaitingAnswer = true;
                    reportViewModel.getReportsWithOffsetLimitAndFilter(numberReports, 5, searchText);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = research.getText().toString();
                numberReports = 0;
                reportAdapter.resetReports();
                reportViewModel.getReportsWithOffsetLimitAndFilter(numberReports, 5, searchText);
            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchText = research.getText().toString();
                if(searchText.equals("")) {
                    numberReports = 0;
                    reportAdapter.resetReports();
                    reportViewModel.getReportsWithOffsetLimitAndFilter(numberReports, 5, searchText);
                }
            }
        };

        research.addTextChangedListener(textWatcher);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        reportViewModel.getReportsWithOffsetLimitAndFilter(numberReports, 5, searchText);
        reportViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            reportsList = new ArrayList<>();
            reportsList.addAll(reports);

            if(reportAdapter.getItemCount() == 0) {
                reportAdapter.setReports(reportsList);
            } else {
                reportAdapter.addReports(reportsList);
            }
            progressBar.setVisibility(View.INVISIBLE);
            numberReports += reportsList.size();
            isWaitingAnswer = false;
        });

        reportViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.error, error.getErrorMessage());
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        numberReports = 0;
    }
}