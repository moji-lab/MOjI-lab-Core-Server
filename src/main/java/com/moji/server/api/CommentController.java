package com.moji.server.api;

import com.moji.server.model.CommentReq;
import com.moji.server.service.BoardService;
import com.moji.server.service.CommentService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.moji.server.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    public CommentController(
            final CommentService commentService,
            final BoardService boardService) {
        this.commentService = commentService;
        this.boardService = boardService;
    }

    /**
     * board 댓글 작성
     * @param commentReq
     * @return
     */
    @Auth
    @PostMapping("/boards")
    public ResponseEntity saveBoardComment(
            @RequestBody CommentReq commentReq,
            HttpServletRequest httpServletRequest) {
        try {
            commentReq.setUserIdx((int) httpServletRequest.getAttribute("userIdx"));
            return new ResponseEntity<>(commentService.saveBoardComment(commentReq),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * course 댓글 작성
     * @param commentReq
     * @return
     */
    @Auth
    @PostMapping("/courses")
    public ResponseEntity saveCourseComment(
            @RequestBody CommentReq commentReq,
            HttpServletRequest httpServletRequest) {
        try {
            commentReq.setUserIdx((int) httpServletRequest.getAttribute("userIdx"));
            return new ResponseEntity<>(commentService.saveCourseComment(commentReq),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
