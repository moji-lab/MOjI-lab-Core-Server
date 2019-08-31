package com.moji.server.api;


import com.moji.server.domain.Board;
import com.moji.server.model.BoardReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(final BoardService boardService)
    {
        this.boardService = boardService;
    }

    //게시물 등록
    @PostMapping("boards")
    public ResponseEntity<DefaultRes> saveBoard(final BoardReq board){
        try{
            return new ResponseEntity<>(boardService.saveBoard(board),HttpStatus.OK);
        } catch (Exception e)
        {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/boards")
    public ResponseEntity<DefaultRes> getRandomBoards() {
        try {
            int userIdx = 1;
            log.info("random feed");
            return new ResponseEntity<>(boardService.getRecentFeed(userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //게시물 공유
    @GetMapping("shares/{person}")
    public  ResponseEntity<DefaultRes> shareBoard(@PathVariable(value = "person") final String person)
    {
        try{
            return new ResponseEntity<>(boardService.shareBoard(person), HttpStatus.OK);
        } catch (Exception e)
        {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
