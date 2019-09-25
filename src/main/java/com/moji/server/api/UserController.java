package com.moji.server.api;

import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SignUpReq;
import com.moji.server.service.UserService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.moji.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created By ds on 2019-08-14.
 * Modified By yw on 2019-08-31.
 */

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<DefaultRes> signUp(@RequestBody final SignUpReq signUpReq) {
        try {
            log.info(signUpReq.toString());
            return new ResponseEntity<>(userService.saveUser(signUpReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/email/check")
    public ResponseEntity<DefaultRes> checkEmail(@RequestParam("email") final String email) {
        try {
            return new ResponseEntity<>(userService.validateEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/nick/check")
    public ResponseEntity<DefaultRes> checkNickname(@RequestParam("nickName") final String nickName) {
        try {
            return new ResponseEntity<>(userService.validateNickName(nickName), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 마이페이지- 프로필사진, 닉네임 데이터 줄때 나의 기록 총 게시글 개수도 같이 보내줘
     * 기본으로 프로필 사진, 닉네임, 나의 기록, 친구와 공유, 스크랩 한 글 갯수 포함
     * 마이 페이지 1 - 나의 기록하기 리스트
     * 마이 페이지 2 - 친구와 공유한 기록하기 리스트
     * 마이 페이지 3 - 스크랩 한 글 리스트
     */

    @Auth
    @GetMapping("/users")
    public ResponseEntity<DefaultRes> getUser(final HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(userService.findByUserIdx(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/users/photoUrl/{userIdx}")
    public ResponseEntity<DefaultRes> getUserPhotoUrl(@PathVariable("userIdx") final int userIdx) {
        try {
            return new ResponseEntity<>(userService.findPhotoUrlByUserIdx(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}