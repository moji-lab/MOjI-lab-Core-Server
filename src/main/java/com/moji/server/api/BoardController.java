package com.moji.server.api;

import com.moji.server.model.BoardReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.BoardService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService) {
        this.boardService = boardService;
    }

    //게시물 등록
    @Auth
    @PostMapping("boards")
    public ResponseEntity<DefaultRes> saveBoard(final BoardReq board,
                                                final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");

            log.info("보드 컨트롤");
//            log.info(board.getInfo().toString());
            log.info("시작");
//            log.info(board.getCourses().toString());
            return new ResponseEntity<>(boardService.saveBoard(board, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/boards")
    public ResponseEntity<DefaultRes> getRandomBoards(final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
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
            final HttpServletRequest httpServletRequest) {
        try {
            int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.getBoardInfo(boardIdx, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //게시물 공유 사람 조회
    @GetMapping("shares")
    public ResponseEntity<DefaultRes> getSharePerson(@RequestParam(value = "person", required = false) final String person) {
        try {
            return new ResponseEntity<>(boardService.getSharePerson(person), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}