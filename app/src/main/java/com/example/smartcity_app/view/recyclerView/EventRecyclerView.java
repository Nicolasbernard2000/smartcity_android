package com.example.smartcity_app.view.recyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcity_app.R;
import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.view.dialog.EventDeleteDialog;
import com.example.smartcity_app.view.dialog.EventOperationDialog;
import com.example.smartcity_app.view.dialog.ParticipationDialog;

import java.util.GregorianCalendar;
import java.util.List;

public class EventRecyclerView {
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView dateHour;
        private TextView duration;
        private TextView description;
        private Button participationButton;
        private LinearLayout creatorButtonsLayout, buttonsLayout;
        private ImageButton deleteButton;
        private ImageButton modifyButton;

        public EventViewHolder(@NonNull View itemView, EventRecyclerView.OnItemSelectedListener participationListener, EventRecyclerView.OnItemSelectedListener deleteListener, EventRecyclerView.OnItemSelectedListener modifyListener) {
            super(itemView);
            dateHour = itemView.findViewById(R.id.event_recycler_item_date_hour);
            duration = itemView.findViewById(R.id.event_recycler_item_duration);
            description = itemView.findViewById(R.id.event_recycler_item_description);
            participationButton = itemView.findViewById(R.id.event_participation_button);
            creatorButtonsLayout = itemView.findViewById(R.id.event_creator_buttons);
            buttonsLayout = itemView.findViewById(R.id.event_buttons);
            deleteButton = itemView.findViewById(R.id.event_delete_button);
            modifyButton = itemView.findViewById(R.id.event_modify_button);

            participationButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                participationListener.onItemSelected(currentPosition);
            });

            deleteButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                deleteListener.onItemSelected(currentPosition);
            });

            modifyButton.setOnClickListener(v -> {
                int currentPosition = getAdapterPosition();
                modifyListener.onItemSelected(currentPosition);
            });

        }
    }

    public static class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<Event> events;
        private ViewGroup parent;
        private Fragment fragment;
        private User user;

        public EventAdapter(Fragment fragment) {
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
                args.putInt("userID", user.getId());
                args.putInt("eventID", event.getId());
                participationDialog.setArguments(args);
                participationDialog.show(fragment.getParentFragmentManager(), null);
            }, position -> {
                Event event = events.get(position);
                EventDeleteDialog eventDeleteDialog = EventDeleteDialog.getInstance();
                eventDeleteDialog.setTargetFragment(fragment, 0);
                eventDeleteDialog.setEvent(event);
                eventDeleteDialog.show(fragment.getParentFragmentManager(), null);
            }, position -> {
                Event event = events.get(position);
                EventOperationDialog eventOperationDialog = new EventOperationDialog(event);
                eventOperationDialog.setTargetFragment(fragment, 0);
                eventOperationDialog.show(fragment.getParentFragmentManager(), null);
            });
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = events.get(position);

            GregorianCalendar dateHour = new GregorianCalendar();
            dateHour.setTime(event.getDateHour());
            int day = dateHour.get(GregorianCalendar.DAY_OF_MONTH);
            int month = dateHour.get(GregorianCalendar.MONTH) + 1;
            int year = dateHour.get(GregorianCalendar.YEAR);
            int hour = dateHour.get(GregorianCalendar.HOUR_OF_DAY);
            int minute = dateHour.get(GregorianCalendar.MINUTE);
            String dateString = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute);

            holder.dateHour.setText(dateString);
            holder.duration.setText(event.getDuration() + " " + parent.getResources().getString(R.string.minutes));
            holder.description.setText(event.getDescription());

            if(user == null) {
                holder.buttonsLayout.removeAllViews();
            }

            if(user == null || !user.getId().equals(event.getCreatorId())) {
                holder.creatorButtonsLayout.removeAllViews();
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

        public void setUser(User user) {
            this.user = user;
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
