package com.orange.callhistory.controller

import com.orange.callhistory.controller.dto.CallDtoR
import com.orange.callhistory.controller.dto.CallDtoW
import com.orange.callhistory.controller.dto.CallEventDto
import com.orange.callhistory.service.Call
import com.orange.callhistory.service.CallEvent
import com.orange.callhistory.service.CallEventStatus.valueOf
import com.orange.callhistory.service.CallService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CallsController(private val callService: CallService) : CallsApi {

    override fun putCalls(@PathVariable callId: String, @RequestBody callDtoW: CallDtoW): ResponseEntity<Void> {

        val call = Call(callId, callDtoW.participantTelNumber, callDtoW.participantAnnouncement, callDtoW.participantRingingTimeout)

        val existingCall = callService.findCall(callId)
        if (existingCall.isPresent) {
            throw CallException("You cannot create a call with this callId : a call with id=$callId already exists")
        } else {
            callService.save(call)
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun postCallEvents(@PathVariable("callId") callId: String, @RequestBody callEventDto: CallEventDto): ResponseEntity<Void> {

        val callEvent = CallEvent(UUID.randomUUID().toString(), valueOf(callEventDto.status.toString()), callEventDto.timestamp)

        val call = callService.findCall(callId)
        if (call.isPresent) {
            call.get().addEvent(callEvent)
            callService.save(call.get())
        } else {
            throw CallException("Cannot add event to call with callId $callId : it does not exist")
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun getCall(@PathVariable("callId") callId: String): ResponseEntity<CallDtoR?> {

        val call = callService.findCall(callId)
        val callDtoR = call.map { mapCallToCallDto(it) }.orElse(null)
        return ResponseEntity(callDtoR, HttpStatus.OK)
    }


    private fun mapCallToCallDto(call: Call): CallDtoR {
        val callDtoR = CallDtoR()
        callDtoR.callId = call.callId
        callDtoR.participantTelNumber = call.participantTelNumber
        callDtoR.participantAnnouncement = call.participantAnnouncement
        callDtoR.participantRingingTimeout = call.participantRingingTimeout
        // the geozone is calculated with the phonenumber prefix
        val participantGeoZone: String
        if (call.participantTelNumber.startsWith("+33")) {
            participantGeoZone = "FR"
        } else if (call.participantTelNumber.startsWith("+34")) {
            participantGeoZone = "SP"
        } else {
            participantGeoZone = "OTHER_COUNTRY"
        }
        callDtoR.participantGeoZone = CallDtoR.ParticipantGeoZoneEnum.fromValue(participantGeoZone)
        callDtoR.connectionDate = call.connectionDate
        callDtoR.terminationDate = call.terminationDate
        return callDtoR
    }

}