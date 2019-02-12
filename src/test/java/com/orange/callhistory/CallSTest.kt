package com.orange.callhistory

import org.junit.Test
import org.junit.runner.RunWith

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.StatusAssertions
import org.springframework.test.web.reactive.server.WebTestClient


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureWebTestClient
class CallSTest {

    @Autowired
    internal var webTestClient: WebTestClient? = null

    @Test
    fun put_a_call() {
        assertPutCallReturnedStatus("call-0", PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent
    }

    @Test
    fun post_a_call_event() {

        assertPutCallReturnedStatus(CALL_ID_2, PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent

        assertThatPostEventReturnedStatus(CALL_ID_2, "CREATED", "2018-10-02T16:00:00.000+02:00")
                .isNoContent
    }

    @Test
    fun put_and_get_a_call() {

        assertPutCallReturnedStatus(CALL_ID, PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent

        assertThatPostEventReturnedStatus(CALL_ID, "CREATED", "2018-10-02T16:00:00+02:00")
                .isNoContent
        assertThatPostEventReturnedStatus(CALL_ID, "RINGING", "2018-10-02T16:00:01+02:00")
                .isNoContent
        assertThatPostEventReturnedStatus(CALL_ID, "CONNECTED", "2018-10-02T16:00:10+02:00")
                .isNoContent
        assertThatPostEventReturnedStatus(CALL_ID, "TERMINATED", "2018-10-02T16:05:10+02:00")
                .isNoContent

        assertGetCallReturnsOk(CALL_ID, "2018-10-02T16:00:10+02:00", "2018-10-02T16:05:10+02:00")
    }

    private fun assertPutCallReturnedStatus(callId: String, participantTelNumber: String, participantAnnouncement: String): StatusAssertions {
        val callBody = "{" +
                "\"participantTelNumber\" : \"" + participantTelNumber + "\"," +
                "\"participantAnnouncement\" : \"" + participantAnnouncement + "\"," +
                "\"participantRingingTimeout\" : \"" + 30 + "\"" +
                " }"
        return webTestClient!!.put()
                .uri("/calls/$callId")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(callBody)
                .exchange()
                .expectStatus()
    }

    private fun assertThatPostEventReturnedStatus(callId: String, eventStatus: String, timestamp: String): StatusAssertions {
        val eventBody = "{ \"status\" : \"$eventStatus\", \"timestamp\" : \"$timestamp\"}"
        return webTestClient!!.post()
                .uri("/calls/$callId/events")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(eventBody)
                .exchange()
                .expectStatus()
    }


    private fun assertGetCallReturnsOk(callId: String, connectionDate: String, terminationDate: String) {
        webTestClient!!.get()
                .uri("/calls/{callId}", callId)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody()
                .jsonPath("$.participantTelNumber").isEqualTo(PARTICIPANT_TEL_NUMBER)
                .jsonPath("$.participantAnnouncement").isEqualTo(RINGBACKTONE_WAV)
                .jsonPath("$.participantRingingTimeout").isEqualTo(30)
                .jsonPath("$.participantGeoZone").isEqualTo(FRENCH_ZONE)
                .jsonPath("$.connectionDate").isEqualTo(connectionDate)
                .jsonPath("$.terminationDate").isEqualTo(terminationDate)
    }

    companion object {


        val PARTICIPANT_TEL_NUMBER = "+33501020304"

        val RINGBACKTONE_WAV = "ringbacktone.wav"

        val CALL_ID = "call-1"

        val FRENCH_ZONE = "FR"

        private val CALL_ID_2 = "call-2"

        private val CALL_ID_3 = "call-3"
    }
}