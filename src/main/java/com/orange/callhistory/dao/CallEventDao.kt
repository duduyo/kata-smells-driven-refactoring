package com.orange.callhistory.dao

import com.orange.callhistory.service.CallEventStatus
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class CallEventDao {
    @Id
    lateinit var id: String

    lateinit var status: CallEventStatus

    lateinit var timestamp: OffsetDateTime

    @ManyToOne
    private var callDao: CallDao? = null

    // constructor needed by hibernate
    constructor() {
    }

    constructor(id: String, status: CallEventStatus, timestamp: OffsetDateTime) {

        this.id = id
        this.status = status
        this.timestamp = timestamp
    }

    internal fun setCallDao(callDao: CallDao) {
        this.callDao = callDao
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as CallEventDao?

        if (if (id != null) id != that!!.id else that!!.id != null) return false
        if (status != that.status) return false
        return if (timestamp != null) timestamp == that.timestamp else that.timestamp == null
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        return result
    }
}
