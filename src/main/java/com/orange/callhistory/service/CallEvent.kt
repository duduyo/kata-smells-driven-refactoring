package com.orange.callhistory.service

import java.time.OffsetDateTime


class CallEvent(val id: String, val status: CallEventStatus, val timestamp: OffsetDateTime)
