package com.moji.server.service;

import com.moji.server.domain.Alarm;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.AlarmRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public AlarmService(final AlarmRepository alarmRepository){
        this.alarmRepository = alarmRepository;
    }

    public DefaultRes getAlarms(final int userIdx){
        Optional<List<Alarm>> alarms = alarmRepository.findByUserIdx(userIdx);
        if(alarms.isPresent()){
            if(alarms.get().size() == 0){ return DefaultRes.res(StatusCode.NOT_FOUND, "알람이 없습니다."); }
        }
        return alarms.map(value -> DefaultRes.res(StatusCode.OK, "알람 조회 성공", value)).orElseGet(() -> DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러"));
    }
}