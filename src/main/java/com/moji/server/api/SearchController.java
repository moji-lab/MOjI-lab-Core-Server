package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.SearchReq;
import com.moji.server.service.SearchService;
import com.moji.server.service.TourApiService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class SearchController {

    private final SearchService searchService;
    private final TourApiService tourApiService;

    public SearchController(final SearchService searchService,
                            final TourApiService tourApiService) {
        this.searchService = searchService;
        this.tourApiService = tourApiService;
    }

    // 검색
    @Auth
    @PostMapping("/searches")
    public ResponseEntity<DefaultRes> search(@RequestBody final SearchReq searchReq,
                                             HttpServletRequest httpServletRequest) {
        try{
            int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            if(searchReq.getKeyword() == null){
                return new ResponseEntity<>(searchService.getAllSearchResult(), HttpStatus.OK);
            }
            else if(searchReq.getKeyword().charAt(0) == '#') {
                String keywordExceptHash = searchReq.getKeyword().substring(1);
                searchReq.setKeyword(keywordExceptHash);
                return new ResponseEntity<>(searchService.getSearchResultByHashtag(searchReq), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(searchService.getSearchResultByPlace(searchReq,userIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}