package com.moji.server.api;

import com.moji.server.model.BoardReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.BoardService;
import com.moji.server.service.CourseService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class BoardController {

    private final BoardService boardService;
    private final CourseService courseService;

    public BoardController(final BoardService boardService,
                           final CourseService courseService) {
        this.boardService = boardService;
        this.courseService = courseService;
    }

    //게시물 등록
    @Auth
    @PostMapping("boards")
    public ResponseEntity<DefaultRes> saveBoard(final BoardReq board, HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int)httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.saveBoard(board, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards")
    public ResponseEntity<DefaultRes> getRandomBoards(HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int)httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.getRecentFeed(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/boards/{boardIdx}")
    public ResponseEntity<DefaultRes> getBoardsInfo(
            @PathVariable(value = "boardIdx") final String boardIdx,
            HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int)httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.getBoardInfo(boardIdx, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //게시물 공유 사람 조회
    @GetMapping("shares/{person}")
    public ResponseEntity<DefaultRes> getSharePerson(@PathVariable(value = "person") final String person) {
        try {
            return new ResponseEntity<>(boardService.getSharePerson(person), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

}
