package com.orange.callhistory.service

import com.orange.callhistory.dao.CallDao
import com.orange.callhistory.dao.CallEventDao
import com.orange.callhistory.dao.CallRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CallService(private val callRepository: CallRepository) {

    fun save(call: Call) {
        val callDao = mapCallToDao(call)
        callRepository.save(callDao)
    }

    private fun mapCallToDao(call: Call): CallDao {
        return CallDao(
                call.callId,
                call.participantTelNumber,
                call.participantAnnouncement,
                call.participantRingingTimeout,
                mapCallEventsToDaos(call))
    }

    private fun mapCallEventsToDaos(call: Call): List<CallEventDao> {
        return call.events.toList().map { mapCallEventToDao(it) }
    }

    private fun mapCallEventToDao(callEvent: CallEvent): CallEventDao {
        return CallEventDao(callEvent.id, callEvent.status, callEvent.timestamp)
    }

    fun findCall(callId: String): Optional<Call> {
        val callDao = callRepository.findById(callId)
        return callDao.map { mapDaoToCall(it) }
    }

    private fun mapDaoToCall(callDao: CallDao): Call {

        return Call(
                callDao.callId,
                callDao.participantUri,
                callDao.participantAnnouncement,
                callDao.participantRingingTimeout,
                mapDaoToCallEvents(callDao),
                callDao.connectionDate,
                callDao.terminationDate)
    }

    private fun mapDaoToCallEvents(callDao: CallDao): List<CallEvent> {
        return callDao.events.map { mapDaoToEvent(it) }.toList()
    }

    private fun mapDaoToEvent(callEventDao: CallEventDao): CallEvent {
        return CallEvent(callEventDao.id, callEventDao.status, callEventDao.timestamp)
    }
}