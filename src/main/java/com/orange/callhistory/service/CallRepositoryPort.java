package com.orange.callhistory.service;

import java.util.Optional;

import com.orange.callhistory.service.entity.Call;

public interface CallRepositoryPort {
    void save(Call call);

    Optional<Call> findById(String callId);
}
