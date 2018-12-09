package com.orange.callhistory.service.entity;

import static com.orange.callhistory.service.entity.CallEventStatus.CONNECTED;
import static com.orange.callhistory.service.entity.CallEventStatus.TERMINATED;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Call {

    private String callId;

    private Participant participant;

    private Set<CallEvent> events = new HashSet<>();

    public Call(String callId, Participant participant, List<CallEvent> events) {
        this(callId, participant);
        events.stream().forEach(this::addEvent);
    }

    public Call(String callId, Participant participant) {
        this.callId = callId;
        this.participant = participant;
    }

    public String getCallId() {
        return callId;
    }

    public void addEvent(CallEvent callEvent) {
        this.events.add(callEvent);
    }

    public List<CallEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public OffsetDateTime getConnectionDate() {
        return getDateFromEventType(CONNECTED);
    }

    private OffsetDateTime getDateFromEventType(CallEventStatus connected) {
        Optional<CallEvent> event = this.getEvents().stream().filter(callEvent -> callEvent.getStatus().equals(connected)).findFirst();
        OffsetDateTime connectedEventDate = event.map(CallEvent::getTimestamp).orElse(null);
        return connectedEventDate;
    }

    public OffsetDateTime getTerminationDate() {
        return getDateFromEventType(TERMINATED);
    }

    public Participant getParticipant() {
        return participant;
    }

}
