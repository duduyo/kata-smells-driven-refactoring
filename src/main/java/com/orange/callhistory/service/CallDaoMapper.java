package com.orange.callhistory.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.orange.callhistory.dao.CallDao;
import com.orange.callhistory.dao.CallEventDao;

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
                callDao.getParticipantUri(),
                callDao.getParticipantAnnouncement(),
                callDao.getParticipantRingingTimeout(),
                mapDaoToCallEvents(callDao));
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
                call.getParticipantTelNumber(),
                call.getParticipantAnnouncement(),
                call.getParticipantRingingTimeout(),
                mapCallEventsToDaos(call));
    }
}
