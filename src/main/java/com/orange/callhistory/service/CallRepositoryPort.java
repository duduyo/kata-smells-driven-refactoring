package com.orange.callhistory.service;

import java.util.Optional;

import com.orange.callhistory.dao.CallDao;
import com.orange.callhistory.dao.CallRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallRepositoryPort {

    private CallDaoMapper callDaoMapper;

    private CallRepository callRepository;

    @Autowired
    public CallRepositoryPort(CallDaoMapper callDaoMapper, CallRepository callRepository) {
        this.callDaoMapper = callDaoMapper;
        this.callRepository = callRepository;
    }

    public void save(Call call) {
        CallDao callDao = callDaoMapper.mapCallToDao(call);
        callRepository.save(callDao);
    }

    public Optional<Call> findById(String callId) {
        Optional<CallDao> callDao = callRepository.findById(callId);
        return callDao.map(callDaoMapper::mapDaoToCall);
    }
}
