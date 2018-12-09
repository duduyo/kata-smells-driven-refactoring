package com.orange.callhistory.dao;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.orange.callhistory.service.entity.Call;
import com.orange.callhistory.service.entity.CallEvent;
import com.orange.callhistory.service.entity.Participant;

import org.springframework.stereotype.Component;

@Component
public class CallDaoMapper {
    CallEvent mapDaoToEvent(CallEventDao callEventDao) {
        return new CallEvent(callEventDao.getId(), callEventDao.getStatus(), callEventDao.getTimestamp());
    }

    List<CallEvent> mapDaoToCallEvents(CallDao callDao) {
        return callDao.getEvents().stream().map(this::mapDaoToEvent).collect(toList());
    }

    Call mapDaoToCall(CallDao callDao) {
        return new Call(
                callDao.getCallId(),
                new Participant(callDao.getParticipantUri(), callDao.getParticipantAnnouncement(), callDao.getParticipantRingingTimeout()), mapDaoToCallEvents(callDao));
    }

    CallEventDao mapCallEventToDao(CallEvent callEvent) {
        return new CallEventDao(callEvent.getId(), callEvent.getStatus(), callEvent.getTimestamp());
    }

    List<CallEventDao> mapCallEventsToDaos(Call call) {
        return call.getEvents().stream().map(this::mapCallEventToDao).collect(toList());
    }

    CallDao mapCallToDao(Call call) {
        return new CallDao(
                call.getCallId(),
                call.getParticipant().getParticipantTelNumber(),
                call.getParticipant().getParticipantAnnouncement(),
                call.getParticipant().getParticipantRingingTimeout(),
                mapCallEventsToDaos(call));
    }
}
