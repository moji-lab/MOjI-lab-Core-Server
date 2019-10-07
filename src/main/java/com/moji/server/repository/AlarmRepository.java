package com.moji.server.repository;

import com.moji.server.domain.Alarm;
import com.moji.server.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends MongoRepository<Alarm, String> {
    Optional<List<Alarm>> findByReceiverIdx(int userIdx);
}