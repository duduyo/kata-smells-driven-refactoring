package com.orange.callhistory;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class CallSTest {


    public static final String PARTICIPANT_TEL_NUMBER = "+33501020304";

    public static final String RINGBACKTONE_WAV = "ringbacktone.wav";

    public static final String CALL_ID = "call-1";

    public static final String FRENCH_ZONE = "FR";

    private static final String CALL_ID_2 = "call-2";

    private static final String CALL_ID_3 = "call-3";

    @Autowired WebTestClient webTestClient;

    @Test
    public void put_a_call() {
        assertPutCallReturnedStatus("call-0", PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent();
    }

    @Test
    public void post_a_call_event() {

        assertPutCallReturnedStatus(CALL_ID_2, PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent();

        assertThatPostEventReturnedStatus(CALL_ID_2, "CREATED", "2018-10-02T16:00:00.000+02:00")
                .isNoContent();
    }

    @Test
    public void put_and_get_a_call() {

        assertPutCallReturnedStatus(CALL_ID, PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent();

        assertThatPostEventReturnedStatus(CALL_ID, "CREATED", "2018-10-02T16:00:00+02:00")
                .isNoContent();
        assertThatPostEventReturnedStatus(CALL_ID, "RINGING", "2018-10-02T16:00:01+02:00")
                .isNoContent();
        assertThatPostEventReturnedStatus(CALL_ID, "CONNECTED", "2018-10-02T16:00:10+02:00")
                .isNoContent();
        assertThatPostEventReturnedStatus(CALL_ID, "TERMINATED", "2018-10-02T16:05:10+02:00")
                .isNoContent();

        assertGetCallReturnsOk(CALL_ID, "2018-10-02T16:00:10+02:00", "2018-10-02T16:05:10+02:00");
    }

    @Test
    public void put_and_get_calls_for_geozones() {

        assertPutCallReturnedStatus("call-4", "+34501020304", RINGBACKTONE_WAV).isNoContent();
        webTestClient.get()
                .uri("/calls/{callId}", "call-4")
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.participantGeoZone").isEqualTo("SP");

        assertPutCallReturnedStatus("call-5", "+35501020304", RINGBACKTONE_WAV).isNoContent();
        webTestClient.get()
                .uri("/calls/{callId}", "call-5")
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.participantGeoZone").isEqualTo("OTHER_COUNTRY");
    }


    @Test
    public void post_event_returns_not_found() {

        assertThatPostEventReturnedStatus("call-unknown", "CREATED", "2018-10-02T16:00:00+02:00")
                .isBadRequest();
    }

    @Test
    public void post_call_with_already_existing_id_returns_bad_request() {

        assertPutCallReturnedStatus(CALL_ID_3, PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isNoContent();

        assertPutCallReturnedStatus(CALL_ID_3, PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV).isBadRequest();
    }


    private StatusAssertions assertPutCallReturnedStatus(String callId, String participantTelNumber, String participantAnnouncement) {
        String callBody =
                "{" +
                        "\"participantTelNumber\" : \"" + participantTelNumber + "\"," +
                        "\"participantAnnouncement\" : \"" + participantAnnouncement + "\"," +
                        "\"participantRingingTimeout\" : \"" + 30 + "\"" +
                        " }";
        return webTestClient.put()
                .uri("/calls/" + callId)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(callBody)
                .exchange()
                .expectStatus();
    }

    private StatusAssertions assertThatPostEventReturnedStatus(String callId, String eventStatus, String timestamp) {
        String eventBody = "{ \"status\" : \"" + eventStatus + "\", \"timestamp\" : \"" + timestamp + "\"}";
        return webTestClient.post()
                .uri("/calls/" + callId + "/events")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(eventBody)
                .exchange()
                .expectStatus();
    }


    private void assertGetCallReturnsOk(String callId, String connectionDate, String terminationDate) {
        webTestClient.get()
                .uri("/calls/{callId}", callId)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.participantTelNumber").isEqualTo(PARTICIPANT_TEL_NUMBER)
                .jsonPath("$.participantAnnouncement").isEqualTo(RINGBACKTONE_WAV)
                .jsonPath("$.participantRingingTimeout").isEqualTo(30)
                .jsonPath("$.participantGeoZone").isEqualTo(FRENCH_ZONE)
                .jsonPath("$.connectionDate").isEqualTo(connectionDate)
                .jsonPath("$.terminationDate").isEqualTo(terminationDate);
    }
}