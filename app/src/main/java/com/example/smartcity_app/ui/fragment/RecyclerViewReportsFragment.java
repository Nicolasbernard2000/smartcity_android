package com.example.smartcity_app.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewReportsFragment extends Fragment {
    public RecyclerViewReportsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view_reports_fragment, container, false);
        RecyclerView reportsRecyclerView = root.findViewById(R.id.report_recycler_view);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //TODO s'amuser à rechercher les infos dans l'API GLHF
        ArrayList<Report> reports = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            Report report = new Report(i, "Ceci est une description lsduifqfffffqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqfqqqhqqqqlqzqeqqjqgqoiuzzzzzzzzz", "En traitement", "Namur", "Rue de la citadelle", 5000, 10, "03 novembre 2021", "Déchêts");
            reports.add(report);
        }

        ReportAdapter reportAdapter = new ReportAdapter(container, reports);
        reportsRecyclerView.setAdapter(reportAdapter);
        return root;
    }

    private static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView location;
        private TextView date;
        private TextView type;
        private TextView address;
        private TextView status;
        private LinearLayout item;
        //ImageView image; //TODO voir ce qu'il faut faire niveau image

        public ReportViewHolder(@NonNull View itemView, OnItemSelectedListener listener) {
            super(itemView);
            location = itemView.findViewById(R.id.report_recycler_item_location);
            date = itemView.findViewById(R.id.report_recycler_item_date);
            type = itemView.findViewById(R.id.report_recycler_item_type);
            address = itemView.findViewById(R.id.report_recycler_item_address);
            status = itemView.findViewById(R.id.report_recycler_item_status);
            item = itemView.findViewById(R.id.report_recycler_item);

            item.setOnClickListener(e -> {
                int currentPosition = getAdapterPosition();
                listener.onItemSelected(currentPosition);
            });
        }
    }

    private static class ReportAdapter extends RecyclerView.Adapter<ReportViewHolder> {
        private List<Report> myReports;
        private ViewGroup container;

        public ReportAdapter(ViewGroup container, List<Report> myReports) {
            this.container = container;
            this.myReports = myReports;
        }

        @NonNull
        @Override
        public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout lv = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.report_row, parent, false);
            ReportViewHolder reportViewHolder = new ReportViewHolder(lv, position -> {
                Report touchedReport = myReports.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("touchedReport", touchedReport);
                Navigation.findNavController(container).navigate(R.id.action_fragment_recycler_view_reports_to_fragment_report, bundle);
            });
            return reportViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
            Report report = myReports.get(position);

            String address = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();

            holder.location.setText(report.getCity());
            holder.date.setText(report.getCreationDate());
            holder.type.setText(report.getType());
            holder.address.setText(address);
            holder.status.setText(report.getStatus());
        }

        @Override
        public int getItemCount() {
            return myReports == null ? 0 : myReports.size();
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
