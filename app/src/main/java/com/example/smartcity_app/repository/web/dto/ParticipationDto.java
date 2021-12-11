package com.example.smartcity_app.repository.web.dto;

import com.squareup.moshi.Json;

public class ParticipationDto {
    @Json(name = "participant")
    private Integer participantID;

    @Json(name = "event")
    private Integer eventID;

    public ParticipationDto(Integer participantID, Integer eventID) {
        this.participantID = participantID;
        this.eventID = eventID;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public Integer getParticipantID() {
        return participantID;
    }

    public void setParticipantID(Integer participantID) {
        this.participantID = participantID;
    }
}
