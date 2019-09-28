package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.HomeReq;
import com.moji.server.service.BoardService;
import com.moji.server.service.HomeService;
import com.moji.server.util.auth.Auth;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Auth
    @GetMapping("/home")
    public ResponseEntity<DefaultRes> getHomeData(HttpServletRequest httpServletRequest) {
        try {
            final String hotCategoryKeyword = "축제";

            ArrayList<String> hotKeywords = new ArrayList<String>(
                    Arrays.asList("힐링", "가을", "1박 2일", "음식", "전어"));
            ArrayList<String> recommendKeywords = new ArrayList<String>(
                    Arrays.asList("1박 2일", "힐링", "1박 2일", "음식", "가을"));

            int userIdx = (int)httpServletRequest.getAttribute("userIdx");

            return new ResponseEntity<>(homeService.getHomeData(userIdx, hotCategoryKeyword, hotKeywords, recommendKeywords), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
