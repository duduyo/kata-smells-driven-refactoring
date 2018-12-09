package com.orange.callhistory.service.entity;

import java.time.OffsetDateTime;


public class CallEvent {

    private String id;

    private CallEventStatus status;

    private OffsetDateTime timestamp;

    public CallEvent(String id, CallEventStatus status, OffsetDateTime timestamp) {
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

}
