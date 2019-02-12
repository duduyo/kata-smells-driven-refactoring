package com.orange.callhistory.service

import java.time.OffsetDateTime

data class Call(val callId: String,
                val participantTelNumber: String,
                val participantAnnouncement: String,
                val participantRingingTimeout: Int,
                private val eventsList: List<CallEvent> = emptyList(),
                var connectionDate: OffsetDateTime? = null,
                var terminationDate: OffsetDateTime? = null
) {

    val events: HashSet<CallEvent> = hashSetOf()

    init {
        eventsList.stream().forEach { this.addEvent(it) }
    }

    fun addEvent(callEvent: CallEvent) {
        this.events.add(callEvent)
    }

}
