package com.orange.callhistory.service.entity;

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

    public String getGeoZone() {
        String participantGeoZone;
        if (participantTelNumber.startsWith("+33")) {
            participantGeoZone = "FR";
        }
        else if (participantTelNumber.startsWith("+34")) {
            participantGeoZone = "SP";
        }
        else {
            participantGeoZone = "OTHER_COUNTRY";
        }
        return participantGeoZone;
    }
}
