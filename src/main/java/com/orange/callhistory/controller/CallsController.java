package com.orange.callhistory.controller;

import static com.orange.callhistory.service.CallEventStatus.valueOf;

import java.util.Optional;
import java.util.UUID;

import com.orange.callhistory.controller.dto.CallDtoR;
import com.orange.callhistory.controller.dto.CallDtoW;
import com.orange.callhistory.controller.dto.CallEventDto;
import com.orange.callhistory.service.Call;
import com.orange.callhistory.service.CallEvent;
import com.orange.callhistory.service.CallService;
import com.orange.callhistory.service.Participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallsController implements CallsApi {

    private CallService callService;

    @Autowired
    public CallsController(CallService callService) {
        this.callService = callService;
    }

    @Override
    public ResponseEntity<Void> putCalls(@PathVariable String callId, @RequestBody CallDtoW callDtoW) {
        Call call = new Call(callId, new Participant(callDtoW.getParticipantTelNumber(), callDtoW.getParticipantAnnouncement(), callDtoW.getParticipantRingingTimeout()));
        callService.createCallIfNotExisting(callId, call);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> postCallEvents(@PathVariable("callId") String callId, @RequestBody CallEventDto callEventDto) {
        CallEvent callEvent = new CallEvent(UUID.randomUUID().toString(), valueOf(callEventDto.getStatus().toString()), callEventDto.getTimestamp());
        callService.createEvent(callId, callEvent);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<CallDtoR> getCall(@PathVariable("callId") String callId) {

        Optional<Call> call = callService.findCall(callId);
        CallDtoR callDtoR = call.map(this::mapCallToCallDto).orElse(null);
        return new ResponseEntity<>(callDtoR, HttpStatus.OK);
    }


    private CallDtoR mapCallToCallDto(Call call) {
        CallDtoR callDtoR = new CallDtoR();
        callDtoR.setCallId(call.getCallId());
        callDtoR.setParticipantTelNumber(call.getParticipant().getParticipantTelNumber());
        callDtoR.setParticipantAnnouncement(call.getParticipant().getParticipantAnnouncement());
        callDtoR.setParticipantRingingTimeout(call.getParticipant().getParticipantRingingTimeout());
        callDtoR.setParticipantGeoZone(CallDtoR.ParticipantGeoZoneEnum.fromValue(call.getParticipant().getGeoZone(call)));
        callDtoR.setConnectionDate(call.getConnectionDate());
        callDtoR.setTerminationDate(call.getTerminationDate());
        return callDtoR;
    }

}