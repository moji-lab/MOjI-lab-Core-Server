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
    public ResponseEntity saveBoard(final BoardReq board,
                                    final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.saveBoard(board, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @GetMapping("/boards")
    public ResponseEntity getRandomBoards(final HttpServletRequest httpServletRequest) {
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
    public ResponseEntity getBoardsInfo(
            @PathVariable(value = "boardIdx") final String boardIdx,
            final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.getBoardInfo(boardIdx, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //게시물 공유 사람 조회
    @GetMapping("shares")
    public ResponseEntity getSharePerson(@RequestParam(value = "person", required = false) final String person) {
        try {
            return new ResponseEntity<>(boardService.getSharePerson(person), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/boards/{boardIdx}/public")
    public ResponseEntity boardPublic(final HttpServletRequest httpServletRequest,
                                  @PathVariable final String boardIdx) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(boardService.boardPublic(boardIdx, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}