package com.orange.callhistory.service;

import java.util.Optional;

import com.orange.callhistory.controller.CallException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    private CallRepositoryPort callRepositoryPort;

    @Autowired
    public CallService(CallRepositoryPort callRepositoryPort) {
        this.callRepositoryPort = callRepositoryPort;
    }

    public Optional<Call> findCall(String callId) {
        return callRepositoryPort.findById(callId);
    }

    public void createCallIfNotExisting(String callId, Call call) {
        Optional<Call> existingCall = findCall(callId);
        if (existingCall.isPresent()) {
            throw new CallException("You cannot create a call with this callId : a call with id=" + callId + " already exists");
        }
        else {
            callRepositoryPort.save(call);
        }
    }

    public void createEvent(String callId, CallEvent callEvent) {
        Optional<Call> call = findCall(callId);
        if (call.isPresent()) {
            call.get().addEvent(callEvent);
            callRepositoryPort.save(call.get());
        }
        else {
            throw new CallException("Cannot add event to call with callId " + callId + " : it does not exist");
        }
    }

}