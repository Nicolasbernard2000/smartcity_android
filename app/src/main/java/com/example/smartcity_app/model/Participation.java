package com.example.smartcity_app.model;

public class Participation {
    private Integer participantID;
    private Integer eventID;

    public Participation(Integer participantID, Integer eventID) {
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

    public boolean isUserRegisteredForEvent() {
        return participantID != null && eventID != null;
    }
}
