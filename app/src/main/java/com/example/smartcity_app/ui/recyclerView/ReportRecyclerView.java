package com.example.smartcity_app.ui.recyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.ui.fragment.RecyclerViewReportsFragment;

import java.util.List;

public class ReportRecyclerView {

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView location;
        private TextView date;
        private TextView type;
        private TextView address;
        private TextView status;
        private LinearLayout item;

        public ReportViewHolder(@NonNull View itemView, ReportRecyclerView.OnItemSelectedListener listener) {
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

            Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.appearance_item_recycler_view);
            itemView.startAnimation(animation);
        }
    }

    public static class ReportAdapter extends RecyclerView.Adapter<ReportRecyclerView.ReportViewHolder> {
        private List<Report> reports;
        private ViewGroup container;

        public ReportAdapter(ViewGroup container, List<Report> reports) {
            this.container = container;
            setReports(reports);
        }

        @NonNull
        @Override
        public ReportRecyclerView.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout lv = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.report_row, parent, false);

            return new ReportRecyclerView.ReportViewHolder(lv, position -> {
                Report touchedReport = reports.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("touchedReport", touchedReport);
                Navigation.findNavController(container).navigate(R.id.action_fragment_recycler_view_reports_to_fragment_report, bundle);
            });
        }

        @Override
        public void onBindViewHolder(@NonNull ReportRecyclerView.ReportViewHolder holder, int position) {
            Report report = reports.get(position);

            String address = report.getStreet() + ", " + report.getHouseNumber() + "\n" + report.getZipCode() + " " + report.getCity();

            holder.location.setText(report.getCity());
            holder.date.setText(report.getCreationDate().toString());
            holder.type.setText(report.getReportType().getLabel());
            holder.address.setText(address);

            Context context = this.container.getContext();
            holder.status.setText(context.getString(context.getResources().getIdentifier("state_" + report.getState(), "string", context.getPackageName())));
        }

        @Override
        public int getItemCount() {
            return reports == null ? 0 : reports.size();
        }

        public void setReports(List<Report> reports) {
            this.reports = reports;

            notifyDataSetChanged();
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
