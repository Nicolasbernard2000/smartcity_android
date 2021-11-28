package com.example.smartcity_app.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.ui.recyclerView.ReportRecyclerView;
import com.example.smartcity_app.viewModels.ReportViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewReportsFragment extends Fragment {
    private ReportViewModel viewModel;

    public RecyclerViewReportsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view_reports_fragment, container, false);

        RecyclerView reportsRecyclerView = root.findViewById(R.id.report_recycler_view);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);


        ReportRecyclerView.ReportAdapter reportAdapter = new ReportRecyclerView.ReportAdapter(container, viewModel.getReports().getValue());
        viewModel.getReports().observe(getViewLifecycleOwner(), reportAdapter::setReports);
        reportsRecyclerView.setAdapter(reportAdapter);

        viewModel.getReportsFromWeb();

        return root;
    }

}