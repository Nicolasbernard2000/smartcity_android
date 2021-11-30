package com.example.smartcity_app.mappers;

import com.example.smartcity_app.model.Event;
import com.example.smartcity_app.repositories.web.dto.EventDto;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    private static EventMapper instance = null;

    private EventMapper() {}

    public static EventMapper getInstance() {
        if(instance == null)
            instance = new EventMapper();
        return instance;
    }

    public Event mapToEvent(EventDto dto) {
        if(dto == null)
            return null;
        Event event = new Event(dto.getId(), dto.getDateHour(), dto.getDuration(), dto.getDescription(), dto.getCreatedAt(), dto.getReportId(), dto.getCreatorId());
        return event;
    }

    public List<Event> mapToEvents(List<EventDto> eventDtos) {
        if(eventDtos == null)
            return null;

        List<Event> events = new ArrayList<>();

        for(EventDto eventDto : eventDtos) {
            events.add(mapToEvent(eventDto));
        }

        return events;
    }

    public EventDto mapToEventDto(Event event) {
        if(event == null)
            return null;
        EventDto eventDto = new EventDto(null, event.getDateHour(), event.getDuration(), event.getDescription(), event.getCreatedAt(), event.getReportId(), event.getCreatorId());
        return eventDto;
    }
}
