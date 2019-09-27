package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.HomeReq;
import com.moji.server.service.BoardService;
import com.moji.server.service.HomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class HomeController {
    private final BoardService boardService;
    private final HomeService homeService;

    public HomeController(final BoardService boardService,
                          final HomeService homeService) {
        this.boardService = boardService;
        this.homeService = homeService;
    }

    // 키워드에 맞는 코스 조회(홈)
    @PostMapping("/home")
    public ResponseEntity<DefaultRes> getHomeData(@RequestBody HomeReq homeReq) {
        try {
            List<String> keywords = homeReq.getKeywords();
            return new ResponseEntity<>(homeService.getCoursesByFixedKeywords(keywords), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
