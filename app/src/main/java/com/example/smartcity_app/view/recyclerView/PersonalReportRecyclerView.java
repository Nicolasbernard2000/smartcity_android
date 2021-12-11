package com.example.smartcity_app.view.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.view.dialog.ReportDeleteDialog;

import java.util.List;

public class PersonalReportRecyclerView {

    public static class PersonalReportViewHolder extends RecyclerView.ViewHolder {
        private TextView location;
        private TextView type;
        private TextView status;
        private ImageButton deleteButton;
        private Context context;

        public PersonalReportViewHolder(@NonNull View itemView, PersonalReportRecyclerView.OnItemSelectedListener listener) {
            super(itemView);
            location = itemView.findViewById(R.id.personal_report_recycler_item_location);
            type = itemView.findViewById(R.id.personal_report_recycler_item_type);
            status = itemView.findViewById(R.id.personal_report_recycler_item_status);
            deleteButton = itemView.findViewById(R.id.personal_report_delete_button);

            deleteButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                listener.onItemSelected(currentPosition);
            });
        }
    }

    public static class PersonalReportAdapter extends RecyclerView.Adapter<PersonalReportViewHolder> {
        private List<Report> reports;
        private ViewGroup container;
        private Fragment fragment;

        public PersonalReportAdapter(ViewGroup container, List<Report> reports, Fragment fragment) {
            this.container = container;
            setReports(reports);
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public PersonalReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout lv = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_report_row, parent, false);

            return new PersonalReportViewHolder(lv, position -> {
                Report report = reports.get(position);
                ReportDeleteDialog reportDeleteDialog = ReportDeleteDialog.getInstance();
                reportDeleteDialog.setTargetFragment(fragment, 0);
                reportDeleteDialog.setReport(report);
                reportDeleteDialog.show(fragment.getParentFragmentManager(), null);
            });
        }

        @Override
        public void onBindViewHolder(@NonNull PersonalReportViewHolder holder, int position) {
            Report report = reports.get(position);

            holder.location.setText(report.getCity());
            holder.type.setText(report.getReportType().getLabel());

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