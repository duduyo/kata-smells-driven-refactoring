package com.orange.callhistory.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Call {

    private String callId;

    private String participantTelNumber;

    private String participantAnnouncement;

    private Integer participantRingingTimeout;

    private OffsetDateTime connectionDate;

    private OffsetDateTime terminationDate;

    private Set<CallEvent> events = new HashSet<>();

    public Call(String callId, String participantTelNumber, String participantAnnouncement, Integer participantRingingTimeout, List<CallEvent> events, OffsetDateTime connectionDate, OffsetDateTime terminationDate) {
        this(callId, participantTelNumber, participantAnnouncement, participantRingingTimeout);
        this.connectionDate = connectionDate;
        this.terminationDate = terminationDate;
        events.stream().forEach(this::addEvent);
    }

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
}
