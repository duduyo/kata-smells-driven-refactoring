package com.orange.callhistory.controller;

import static com.orange.callhistory.service.CallEventStatus.valueOf;

import java.util.Optional;
import java.util.UUID;

import com.orange.callhistory.controller.dto.CallDTORead;
import com.orange.callhistory.controller.dto.CallDTOWrite;
import com.orange.callhistory.controller.dto.CallEventDTO;
import com.orange.callhistory.service.Call;
import com.orange.callhistory.service.CallEvent;
import com.orange.callhistory.service.CallService;

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
    public ResponseEntity<Void> putCalls(@PathVariable String callId, @RequestBody CallDTOWrite callDTOWrite) {
        Call call = new Call(callId, callDTOWrite.getParticipantTelNumber(), callDTOWrite.getParticipantAnnouncement(), callDTOWrite.getParticipantRingingTimeout());
        callService.createCallIfNotExisting(callId, call);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> postCallEvents(@PathVariable("callId") String callId, @RequestBody CallEventDTO callEventDTO) {
        CallEvent callEvent = new CallEvent(valueOf(callEventDTO.getStatus().toString()), callEventDTO.getTimestamp());
        callService.createEvent(callId, callEvent);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<CallDTORead> getCall(@PathVariable("callId") String callId) {

        Optional<Call> call = callService.findCall(callId);
        CallDTORead callDtoR = call.map(this::mapCallToCallDto).orElse(null);
        return new ResponseEntity<>(callDtoR, HttpStatus.OK);
    }


    private CallDTORead mapCallToCallDto(Call call) {
        CallDTORead callDTORead = new CallDTORead();
        callDTORead.setCallId(call.getCallId());
        callDTORead.setParticipantTelNumber(call.getParticipantTelNumber());
        callDTORead.setParticipantAnnouncement(call.getParticipantAnnouncement());
        callDTORead.setParticipantRingingTimeout(call.getParticipantRingingTimeout());
        callDTORead.setParticipantGeoZone(CallDTORead.ParticipantGeoZoneEnum.fromValue(call.calculateGeoZone()));
        callDTORead.setConnectionDate(call.getConnectionDate());
        callDTORead.setTerminationDate(call.getTerminationDate());
        return callDTORead;
    }

}