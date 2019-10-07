package com.moji.server.service;

import com.moji.server.domain.Alarm;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.AlarmRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserService userService;

    public AlarmService(final AlarmRepository alarmRepository,
                        final UserService userService) {
        this.alarmRepository = alarmRepository;
        this.userService = userService;
    }

    public DefaultRes getAlarms(final int userIdx) {
        Optional<List<Alarm>> alarms = alarmRepository.findByReceiverIdx(userIdx);
        if (alarms.isPresent() && alarms.get().size() == 0) {
            return DefaultRes.res(StatusCode.NOT_FOUND, "알람이 없습니다.");
        }
        for (Alarm alarm : alarms.get()) {
            alarm.setSenderPhotoUrl(userService.findPhotoUrlByUserIdx(alarm.getSenderIdx()).get());
        }
        return alarms.map(value -> DefaultRes.res(StatusCode.OK, "알람 조회 성공", value)).orElseGet(() -> DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러"));
    }

    public DefaultRes saveAlarm(final Alarm alarm) {
        try {
            alarmRepository.save(alarm);
            return DefaultRes.res(StatusCode.CREATED, "알람 저장 성공");
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}