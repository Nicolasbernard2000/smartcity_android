package com.example.smartcity_app.service.mappers;

import com.example.smartcity_app.model.Participation;
import com.example.smartcity_app.repository.web.dto.ParticipationDto;

public class ParticipationMapper {
    private static ParticipationMapper instance = null;

    private ParticipationMapper() {}

    public static ParticipationMapper getInstance() {
        if(instance == null)
            instance = new ParticipationMapper();
        return instance;
    }

    public Participation mapToParticipation(ParticipationDto dto) {
        if(dto == null)
            return null;

        Participation participation = new Participation(dto.getParticipantID(), dto.getEventID());
        return participation;
    }

    public ParticipationDto mapToParticipationDto(Participation participation) {
        if(participation == null)
            return null;

        ParticipationDto participationDto = new ParticipationDto(participation.getParticipantID(), participation.getEventID());
        return participationDto;
    }
}
