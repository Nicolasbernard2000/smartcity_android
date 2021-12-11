package com.example.smartcity_app.view.recyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.view.MainActivity;
import com.example.smartcity_app.view.dialog.ParticipationDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EventRecyclerView {
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView dateHour;
        private TextView duration;
        private TextView description;
        private Button participationButton;

        public EventViewHolder(@NonNull View itemView, EventRecyclerView.OnItemSelectedListener listener) {
            super(itemView);
            dateHour = itemView.findViewById(R.id.event_recycler_item_date_hour);
            duration = itemView.findViewById(R.id.event_recycler_item_duration);
            description = itemView.findViewById(R.id.event_recycler_item_description);
            participationButton = itemView.findViewById(R.id.event_participation_button);

            participationButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                listener.onItemSelected(currentPosition);
            });
        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<Event> events;
        private ViewGroup parent;
        private Fragment fragment;

        public EventAdapter(List<Event> events, Fragment fragment) {
            this.events = events;
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout lv = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
            this.parent = parent;
            return new EventViewHolder(lv, position -> {
                Event event = events.get(position);
                ParticipationDialog participationDialog = ParticipationDialog.getInstance();
                participationDialog.setTargetFragment(fragment, 0);

                Bundle args = new Bundle();
                args.putInt("userID", MainActivity.getUser().getId());
                args.putInt("eventID", event.getId());
                participationDialog.setArguments(args);
                participationDialog.show(fragment.getParentFragmentManager(), null);
            });
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = events.get(position);

            Date date = event.getDateHour();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            String dateHourText = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);

            holder.dateHour.setText(dateHourText);
            holder.duration.setText(event.getDuration() + " " + parent.getResources().getString(R.string.minutes));
            holder.description.setText(event.getDescription());

            if(!MainActivity.isUserConnected()) {
                holder.participationButton.setVisibility(Button.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return events == null ? 0 : events.size();
        }

        public void setEvents(List<Event> events) {
            this.events = events;
            notifyDataSetChanged();
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
