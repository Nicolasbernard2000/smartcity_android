package com.example.smartcity_app.util;

import com.example.smartcity_app.model.Participation;

public interface CallbackParticipationModification {
    void actionParticipation(Participation participation, boolean wasAlreadyRegistered);
}
