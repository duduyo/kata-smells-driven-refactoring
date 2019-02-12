package com.orange.callhistory.repository

import com.orange.callhistory.dao.CallDao
import com.orange.callhistory.dao.CallEventDao
import com.orange.callhistory.dao.CallRepository
import com.orange.callhistory.service.CallEventStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.OffsetDateTime.parse
import java.util.Arrays.asList
import java.util.Collections.EMPTY_LIST


@RunWith(SpringRunner::class)
@DataJpaTest
open class CallRepositoryITest {

    @Autowired
    internal var callRepository: CallRepository? = null

    @Test
    fun save_and_find_call_by_id() {
        val callEvent = CallEventDao("CEID-1", CallEventStatus.CREATED, parse("2018-10-02T16:00:00.000+02:00"))
        val expectedCall = CallDao("CID", PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV, 30, asList(callEvent))
        expectedCall.addEvent(CallEventDao("CEID-2", CallEventStatus.TERMINATED, parse("2018-10-02T16:00:10.000+02:00")))

        val call = CallDao("CID", PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV, 30, EMPTY_LIST as List<CallEventDao>)
        call.addEvent(CallEventDao("CEID-1", CallEventStatus.CREATED, parse("2018-10-02T16:00:00.000+02:00")))
        call.addEvent(CallEventDao("CEID-2", CallEventStatus.TERMINATED, parse("2018-10-02T16:00:10.000+02:00")))
        callRepository!!.save(call)

        val resultCall = callRepository!!.findById("CID")

        assertEquals(expectedCall, resultCall.get())
    }

    companion object {

        val PARTICIPANT_TEL_NUMBER = "+33501020304"

        val RINGBACKTONE_WAV = "ringbacktone.wav"
    }

}