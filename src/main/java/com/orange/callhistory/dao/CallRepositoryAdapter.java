package com.orange.callhistory.dao;

import java.util.Optional;

import com.orange.callhistory.service.CallRepositoryPort;
import com.orange.callhistory.service.entity.Call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallRepositoryAdapter implements CallRepositoryPort {

    private CallDaoMapper callDaoMapper;

    private CallRepository callRepository;

    @Autowired
    public CallRepositoryAdapter(CallDaoMapper callDaoMapper, CallRepository callRepository) {
        this.callDaoMapper = callDaoMapper;
        this.callRepository = callRepository;
    }

    @Override
    public void save(Call call) {
        CallDao callDao = callDaoMapper.mapCallToDao(call);
        callRepository.save(callDao);
    }

    @Override
    public Optional<Call> findById(String callId) {
        Optional<CallDao> callDao = callRepository.findById(callId);
        return callDao.map(callDaoMapper::mapDaoToCall);
    }
}
