package com.example.smartcity_app.ui.recyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;

import java.util.List;

public class EventRecyclerView {
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView dateHour;
        private TextView duration;
        private TextView description;
        private TextView createdAt;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            dateHour = itemView.findViewById(R.id.event_recycler_item_date_hour);
            duration = itemView.findViewById(R.id.event_recycler_item_duration);
            description = itemView.findViewById(R.id.event_recycler_item_description);
            createdAt = itemView.findViewById(R.id.event_recycler_item_created_at);
        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<Event> events;

        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout lv = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);

            return new EventViewHolder(lv);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = events.get(position);
            Log.i("Debug", event + "");
            holder.dateHour.setText(event.getDateHour().toString());
            holder.duration.setText(event.getDuration() + "");
            holder.createdAt.setText(event.getCreatedAt().toString());
            holder.description.setText(event.getDescription());
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
}
