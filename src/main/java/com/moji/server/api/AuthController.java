package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.LoginReq;
import com.moji.server.service.AuthService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.moji.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created By ds on 2019-08-20.
 */

@Slf4j
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity test() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultRes> login(@RequestBody final LoginReq loginReq) {
        try {
            log.info("로그인 : " + loginReq.toString());
            return new ResponseEntity<>(authService.login(loginReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/auth-check")
    public ResponseEntity authCheck(HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>("인증 성공" , HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}