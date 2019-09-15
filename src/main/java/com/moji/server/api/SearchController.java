package com.moji.server.api;

import com.moji.server.model.DefaultRes;
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
    @GetMapping("/searches")
    public ResponseEntity<DefaultRes> search(@RequestParam final String keyword) {
        try{
            if(keyword.charAt(0) == '#') {
                String keywordExceptHash = keyword.substring(1);
                return new ResponseEntity<>(searchService.getSearchResultByHashtag(keywordExceptHash), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(searchService.getSearchResultByPlace(keyword), HttpStatus.OK);

        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}