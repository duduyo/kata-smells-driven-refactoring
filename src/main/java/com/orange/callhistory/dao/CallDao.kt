package com.orange.callhistory.dao

import com.orange.callhistory.service.CallEventStatus.CONNECTED
import com.orange.callhistory.service.CallEventStatus.TERMINATED
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class CallDao {

    @Id
    lateinit var callId: String

    lateinit var participantUri: String

    lateinit var participantAnnouncement: String

    var participantRingingTimeout: Int = 0

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "callDao")
    @Cascade(CascadeType.ALL)
    var events = mutableListOf<CallEventDao>()

    val connectionDate: OffsetDateTime?
        get() {
            val event = this.events.filter { callEventDao -> callEventDao.status == CONNECTED }.firstOrNull()
            return event?.timestamp
        }

    val terminationDate: OffsetDateTime?
        get() {
            val event = this.events.filter { callEventDao -> callEventDao.status == TERMINATED }.firstOrNull()
            return event?.timestamp
        }

    // default constructor for hibernate
    constructor() {}

    constructor(callId: String, participantUri: String, participantAnnouncement: String, firstParticipantRingingTimeout: Int, events: List<CallEventDao>) : this(callId, participantUri, participantAnnouncement, firstParticipantRingingTimeout) {
        events.stream().forEach { this.addEvent(it) }
    }

    constructor(callId: String, participantUri: String, participantAnnouncement: String, participantRingingTimeout: Int) {

        this.callId = callId
        this.participantUri = participantUri
        this.participantAnnouncement = participantAnnouncement
        this.participantRingingTimeout = participantRingingTimeout
    }

    fun addEvent(callEvent: CallEventDao) {
        this.events.add(callEvent)
        callEvent.setCallDao(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CallDao

        if (callId != other.callId) return false
        if (participantUri != other.participantUri) return false
        if (participantAnnouncement != other.participantAnnouncement) return false
        if (participantRingingTimeout != other.participantRingingTimeout) return false
        if (events != other.events) return false

        return true
    }

    override fun hashCode(): Int {
        var result = callId.hashCode()
        result = 31 * result + participantUri.hashCode()
        result = 31 * result + participantAnnouncement.hashCode()
        result = 31 * result + participantRingingTimeout
        result = 31 * result + events.hashCode()
        return result
    }


}
