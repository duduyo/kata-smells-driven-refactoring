package com.orange.callhistory.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.orange.callhistory.dao.CallDao;
import com.orange.callhistory.dao.CallRepository;
import org.junit.Test;

public class CallServiceUTest {

    private final CallRepository callRepository = mock(CallRepository.class);

    private final CallService callService = new CallService(callRepository);

    private final Call call = new Call("C1", null, null, null);

    @Test
    public void should_save_a_call() {
        callService.save(call);
        verify(callRepository).save(any(CallDao.class));
    }


    @Test
    public void should_find_a_call() {
        callService.findCall("C1");
        verify(callRepository).findById("C1");
    }
}