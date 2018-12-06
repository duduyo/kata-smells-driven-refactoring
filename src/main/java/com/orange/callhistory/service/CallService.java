package com.orange.callhistory.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import com.orange.callhistory.dao.CallDao;
import com.orange.callhistory.dao.CallEventDao;
import com.orange.callhistory.dao.CallRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    private CallRepository callRepository;

    @Autowired
    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public void save(Call call) {
        CallDao callDao = mapCallToDao(call);
        callRepository.save(callDao);
    }

    private CallDao mapCallToDao(Call call) {
        return new CallDao(call.getCallId(), call.getParticipantTelNumber(), call.getParticipantAnnouncement(), call.getParticipantRingingTimeout(), mapCallEventsToDaos(call));
    }

    private List<CallEventDao> mapCallEventsToDaos(Call call) {
        return call.getEvents().stream().map(this::mapCallEventToDao).collect(toList());
    }

    private CallEventDao mapCallEventToDao(CallEvent callEvent) {
        return new CallEventDao(callEvent.getId(), callEvent.getStatus(), callEvent.getTimestamp());
    }

    // see if we should maintain this code
    public void createCallEndpoint(String callId, CallEvent callEvent) {
        Optional<Call> call = findCallById(callId);
        if (call.isPresent()) {
            call.get().addEvent(callEvent);
            Call call1 = call.get();
            CallDao callDao = new CallDao(call1.getCallId(), call1.getParticipantTelNumber(), call1.getParticipantAnnouncement(), call1.getParticipantRingingTimeout());
            callRepository.save(callDao);

        }
    }

    public Optional<Call> findCall(String callId) {
        Optional<Call> call = findCallById(callId);
        return call;
    }

    private Optional<Call> findCallById(String callId) {
        Optional<CallDao> callDao = callRepository.findById(callId);
        Optional<Call> call = callDao.map(this::mapDaoToCall);
        return call;
    }

    private Call mapDaoToCall(CallDao callDao) {
        return new Call(callDao.getCallId(), callDao.getParticipantUri(), callDao.getParticipantAnnouncement(), callDao.getParticipantRingingTimeout(), mapDaoToCallEvents(callDao), callDao.getConnectionDate(), callDao.getTerminationDate());
    }

    private List<CallEvent> mapDaoToCallEvents(CallDao callDao) {
        return callDao.getEvents().stream().map(this::mapDaoToEvent).collect(toList());
    }

    private CallEvent mapDaoToEvent(CallEventDao callEventDao) {
        return new CallEvent(callEventDao.getId(), callEventDao.getStatus(), callEventDao.getTimestamp());
    }
}