package com.orange.callhistory.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO Smell anemic model
public class Call {

    private String callId;

    // TODO Smell : several fields about "participant"
    private String participantTelNumber;

    private String participantAnnouncement;

    private Integer participantRingingTimeout;

    private OffsetDateTime connectionDate;

    private OffsetDateTime terminationDate;

    private Set<CallEvent> events = new HashSet<>();

    // TODO Smell : many params
    public Call(String callId, String participantTelNumber, String participantAnnouncement, Integer participantRingingTimeout, List<CallEvent> events, OffsetDateTime connectionDate, OffsetDateTime terminationDate) {
        this(callId, participantTelNumber, participantAnnouncement, participantRingingTimeout);
        this.connectionDate = connectionDate;
        this.terminationDate = terminationDate;
        events.stream().forEach(this::addEvent);
    }

    // TODO Smell : many params
    public Call(String callId, String participantTelNumber, String participantAnnouncement, Integer participantRingingTimeout) {
        this.callId = callId;
        this.participantTelNumber = participantTelNumber;
        this.participantAnnouncement = participantAnnouncement;
        this.participantRingingTimeout = participantRingingTimeout;
    }

    public String getCallId() {
        return callId;
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

    public void addEvent(CallEvent callEvent) {
        this.events.add(callEvent);
    }

    public List<CallEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public OffsetDateTime getConnectionDate() {
        return connectionDate;
    }

    public OffsetDateTime getTerminationDate() {
        return terminationDate;
    }

    public String calculateGeoZone() {
        String participantGeoZone;
        if (getParticipantTelNumber().startsWith("+33")) {
            participantGeoZone = "FR";
        }
        else if (getParticipantTelNumber().startsWith("+34")) {
            participantGeoZone = "SP";
        }
        else {
            participantGeoZone = "OTHER_COUNTRY";
        }
        return participantGeoZone;
    }
}
