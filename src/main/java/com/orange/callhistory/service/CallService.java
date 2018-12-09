package com.orange.callhistory.service;

import java.util.Optional;

import com.orange.callhistory.controller.CallException;
import com.orange.callhistory.dao.CallDao;
import com.orange.callhistory.dao.CallRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallService {

    private CallRepository callRepository;

    private CallDaoMapper callDaoMapper;

    @Autowired
    public CallService(CallRepository callRepository, CallDaoMapper callDaoMapper) {
        this.callRepository = callRepository;
        this.callDaoMapper = callDaoMapper;
    }

    public void save(Call call) {
        CallDao callDao = callDaoMapper.mapCallToDao(call);
        callRepository.save(callDao);
    }

    public Optional<Call> findCall(String callId) {
        Optional<CallDao> callDao = callRepository.findById(callId);
        Optional<Call> call = callDao.map(callDaoMapper::mapDaoToCall);
        return call;
    }

    public void createCallIfNotExisting(String callId, Call call) {
        Optional<Call> existingCall = findCall(callId);
        if (existingCall.isPresent()) {
            throw new CallException("You cannot create a call with this callId : a call with id=" + callId + " already exists");
        }
        else {
            save(call);
        }
    }

    public void createEvent(String callId, CallEvent callEvent) {
        Optional<Call> call = findCall(callId);
        if (call.isPresent()) {
            call.get().addEvent(callEvent);
            save(call.get());
        }
        else {
            throw new CallException("Cannot add event to call with callId " + callId + " : it does not exist");
        }
    }

}