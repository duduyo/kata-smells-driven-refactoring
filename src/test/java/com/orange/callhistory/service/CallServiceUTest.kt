package com.orange.callhistory.service

import com.orange.callhistory.dao.CallDao
import com.orange.callhistory.dao.CallRepository
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CallServiceUTest {

    private val callRepository = mock<CallRepository>(CallRepository::class.java)

    private val callService = CallService(callRepository)

    private val call = Call("C1", "", "", 0)

    @Test
    fun should_save_a_call() {
        callService.save(call)
        verify(callRepository).save(any(CallDao::class.java!!))
    }


    @Test
    fun should_find_a_call() {
        callService.findCall("C1")
        verify(callRepository).findById("C1")
    }
}