package com.orange.callhistory.controller;

import static com.orange.callhistory.service.CallEventStatus.valueOf;

import java.util.Optional;

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

        Optional<Call> existingCall = callService.findCall(callId);
        if (existingCall.isPresent()) {
            throw new CallException("You cannot create a call with this callId : a call with id=" + callId + " already exists");
        }
        else {
            callService.save(call);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> postCallEvents(@PathVariable("callId") String callId, @RequestBody CallEventDTO callEventDTO) {

        CallEvent callEvent = new CallEvent(valueOf(callEventDTO.getStatus().toString()), callEventDTO.getTimestamp());

        Optional<Call> call = callService.findCall(callId);
        if (call.isPresent()) {
            call.get().addEvent(callEvent);
            callService.save(call.get());
        }
        else {
            throw new CallException("Cannot add event to call with callId " + callId + " : it does not exist");
        }
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
        // the geozone is calculated with the phonenumber prefix
        String participantGeoZone;
        if (call.getParticipantTelNumber().startsWith("+33")) {
            participantGeoZone = "FR";
        }
        else if (call.getParticipantTelNumber().startsWith("+34")) {
            participantGeoZone = "SP";
        }
        else {
            participantGeoZone = "OTHER_COUNTRY";
        }
        callDTORead.setParticipantGeoZone(CallDTORead.ParticipantGeoZoneEnum.fromValue(participantGeoZone));
        callDTORead.setConnectionDate(call.getConnectionDate());
        callDTORead.setTerminationDate(call.getTerminationDate());
        return callDTORead;
    }

}