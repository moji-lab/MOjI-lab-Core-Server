package com.moji.server.api;

import com.moji.server.domain.Alarm;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.AlarmService;
import com.moji.server.service.AuthService;
import com.moji.server.service.UserService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(final AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Auth
    @GetMapping("/alarms")
    public ResponseEntity<DefaultRes> getUserAlarms(final HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(alarmService.getAlarms(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/alarms")
    public ResponseEntity<DefaultRes> saveUserAlarm(final HttpServletRequest httpServletRequest,
                                                    final @RequestBody Alarm alarm) {
        try {
            int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            alarm.setSenderIdx(userIdx);
            return new ResponseEntity<>(alarmService.saveAlarm(userIdx, alarm), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}