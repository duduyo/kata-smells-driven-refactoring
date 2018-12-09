package com.orange.callhistory.service;

public class Participant {
    private final String participantTelNumber;

    private final String participantAnnouncement;

    private final Integer participantRingingTimeout;

    public Participant(String participantTelNumber, String participantAnnouncement, Integer participantRingingTimeout) {
        this.participantTelNumber = participantTelNumber;
        this.participantAnnouncement = participantAnnouncement;
        this.participantRingingTimeout = participantRingingTimeout;
    }

    public String getParticipantTelNumber() {
        return participantTelNumber;
    }

    public String getParticipantAnnouncement() {
        return participantAnnouncement;
    }

    public Integer getParticipantRingingTimeout() {
        return participantRingingTimeout;
    }
}
