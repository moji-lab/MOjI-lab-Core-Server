package com.moji.server.api;

import com.moji.server.model.LikeReq;
import com.moji.server.service.LikeService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.moji.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    @Auth
    @PostMapping("/boards")
    public ResponseEntity saveBoardComment(
            @RequestBody LikeReq likeReq,
            HttpServletRequest httpServletRequest) {
        try {
            likeReq.setUserIdx((int)httpServletRequest.getAttribute("userIdx"));
            return new ResponseEntity<>(likeService.checkBoardLike(likeReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PostMapping("/courses")
    public ResponseEntity saveCourseComment(
            @RequestBody LikeReq likeReq,
            HttpServletRequest httpServletRequest) {
        try {
            likeReq.setUserIdx((int)httpServletRequest.getAttribute("userIdx"));
            return new ResponseEntity<>(likeService.checkCourseLike(likeReq), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
