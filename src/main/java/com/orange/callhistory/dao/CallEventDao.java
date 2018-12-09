package com.orange.callhistory.dao;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.orange.callhistory.service.entity.CallEventStatus;

@Entity
public class CallEventDao {
    @Id
    private String id;

    private CallEventStatus status;

    private OffsetDateTime timestamp;

    @ManyToOne
    private CallDao callDao;

    // constructor needed by hibernate
    public CallEventDao() {
    }

    public CallEventDao(String id, CallEventStatus status, OffsetDateTime timestamp) {

        this.id = id;
        this.status = status;
        this.timestamp = timestamp;
    }


    public String getId() {
        return id;
    }

    public CallEventStatus getStatus() {
        return status;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    void setCallDao(CallDao callDao) {
        this.callDao = callDao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallEventDao that = (CallEventDao) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != that.status) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
