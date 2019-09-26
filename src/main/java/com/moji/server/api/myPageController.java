package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.service.MyPageService;
import com.moji.server.service.UserService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.moji.server.model.DefaultRes.BAD_REQUEST;
import static com.moji.server.model.DefaultRes.FAIL_DEFAULT_RES;

/**
 * Created By ds on 25/09/2019.
 */

@Slf4j
@RestController
public class myPageController {

    private final MyPageService myPageService;

    public myPageController(final MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    /**
     * 마이페이지- 프로필사진, 닉네임 데이터 줄때 나의 기록 총 게시글 개수도 같이 보내줘
     * 기본으로 프로필 사진, 닉네임, 나의 기록, 친구와 공유, 스크랩 한 글 갯수 포함
     * 마이 페이지 1 - 나의 기록하기 리스트 & 친구와 공유한 기록하기 리스트
     * 마이 페이지 2 - 스크랩 한 글 리스트
     */

    @Auth
    @GetMapping("/mypage/{index}")
    public ResponseEntity<DefaultRes> getMyPage(final HttpServletRequest httpServletRequest,
                                                @PathVariable final String index) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            DefaultRes res = null;
            switch (index) {
                case "1": {
                    //res = myPageService.getMyCourseList(user);
                    break;
                }
                case "2": {
                    res = new DefaultRes(2, "2");
                    break;
                }
                default: {
                    return new ResponseEntity<>(BAD_REQUEST, HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
