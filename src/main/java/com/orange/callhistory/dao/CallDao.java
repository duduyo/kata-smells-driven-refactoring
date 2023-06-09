package com.orange.callhistory.dao;

import static com.orange.callhistory.service.CallEventStatus.CONNECTED;
import static com.orange.callhistory.service.CallEventStatus.TERMINATED;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
public class CallDao {

    @Id
    private String callId;

    private String participantUri;

    private String participantAnnouncement;

    private Integer participantRingingTimeout;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "callDao")
    @Cascade(CascadeType.ALL)
    private Set<CallEventDao> events = new HashSet<>();

    // default constructor for hibernate
    public CallDao() {
    }

    public CallDao(String callId, String participantUri, String participantAnnouncement, Integer firstParticipantRingingTimeout, List<CallEventDao> events) {
        this(callId, participantUri, participantAnnouncement, firstParticipantRingingTimeout);
        events.stream().forEach(this::addEvent);
    }

    public CallDao(String callId, String participantUri, String participantAnnouncement, Integer participantRingingTimeout) {

        this.callId = callId;
        this.participantUri = participantUri;
        this.participantAnnouncement = participantAnnouncement;
        this.participantRingingTimeout = participantRingingTimeout;
    }


    public String getCallId() {
        return callId;
    }

    public String getParticipantUri() {
        return participantUri;
    }

    public String getParticipantAnnouncement() {
        return participantAnnouncement;
    }

    public Integer getParticipantRingingTimeout() {
        return participantRingingTimeout;
    }

    public void addEvent(CallEventDao callEvent) {
        this.events.add(callEvent);
        callEvent.setCallDao(this);
    }

    public List<CallEventDao> getEvents() {
        return new ArrayList<>(events);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallDao callDao = (CallDao) o;

        if (participantRingingTimeout != callDao.participantRingingTimeout) return false;
        if (!callId.equals(callDao.callId)) return false;
        if (!participantUri.equals(callDao.participantUri)) return false;
        if (!participantAnnouncement.equals(callDao.participantAnnouncement)) return false;
        return events.equals(callDao.events);
    }

    @Override
    public int hashCode() {
        return callId.hashCode();
    }

    public OffsetDateTime getConnectionDate() {
        Optional<CallEventDao> event = this.getEvents().stream().filter(callEventDao -> callEventDao.getStatus().equals(CONNECTED)).findFirst();
        OffsetDateTime connectedEventDate = event.map(CallEventDao::getTimestamp).orElse(null);
        return connectedEventDate;
    }

    public OffsetDateTime getTerminationDate() {
        Optional<CallEventDao> event = this.getEvents().stream().filter(callEventDao -> callEventDao.getStatus().equals(TERMINATED)).findFirst();
        OffsetDateTime terminatedEventDate = event.map(CallEventDao::getTimestamp).orElse(null);
        return terminatedEventDate;
    }
}
