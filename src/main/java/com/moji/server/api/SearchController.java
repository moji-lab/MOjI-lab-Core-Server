package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.SearchReq;
import com.moji.server.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    // 검색
    @PostMapping("/searches")
    public ResponseEntity<DefaultRes> search(@RequestBody final SearchReq searchReq) {
        try{
            if(searchReq.getKeyword().charAt(0) == '#') {
                String keywordExceptHash = searchReq.getKeyword().substring(1);
                searchReq.setKeyword(keywordExceptHash);
                return new ResponseEntity<>(searchService.getSearchResultByHashtag(searchReq), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(searchService.getSearchResultByPlace(searchReq), HttpStatus.OK);
        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}