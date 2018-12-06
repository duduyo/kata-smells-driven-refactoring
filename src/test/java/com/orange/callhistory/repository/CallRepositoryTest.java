package com.orange.callhistory.repository;

import static java.time.OffsetDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.orange.callhistory.dao.CallDao;
import com.orange.callhistory.dao.CallEventDao;
import com.orange.callhistory.dao.CallRepository;
import com.orange.callhistory.service.CallEventStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CallRepositoryTest {

    public static final String PARTICIPANT_TEL_NUMBER = "+33501020304";

    public static final String RINGBACKTONE_WAV = "ringbacktone.wav";

    @Autowired
    CallRepository callRepository;

    @Test
    public void save_and_find_call_by_id() {
        CallEventDao callEvent = new CallEventDao("CEID-1", CallEventStatus.CREATED, parse("2018-10-02T16:00:00.000+02:00"));
        CallDao expectedCall = new CallDao("CID", PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV, 30, asList(callEvent));
        expectedCall.addEvent(new CallEventDao("CEID-2", CallEventStatus.TERMINATED, parse("2018-10-02T16:00:10.000+02:00")));

        CallDao call = new CallDao("CID", PARTICIPANT_TEL_NUMBER, RINGBACKTONE_WAV, 30, EMPTY_LIST);
        call.addEvent(new CallEventDao("CEID-1", CallEventStatus.CREATED, parse("2018-10-02T16:00:00.000+02:00")));
        call.addEvent(new CallEventDao("CEID-2", CallEventStatus.TERMINATED, parse("2018-10-02T16:00:10.000+02:00")));
        callRepository.save(call);

        Optional<CallDao> resultCall = callRepository.findById("CID");

        assertThat(resultCall.get()).isEqualTo(expectedCall);
    }

}