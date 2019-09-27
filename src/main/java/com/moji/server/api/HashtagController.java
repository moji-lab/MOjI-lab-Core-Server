package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.HashtagReq;
import com.moji.server.service.HashtagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class HashtagController {

    private final HashtagService hashtagService;

    public HashtagController(final HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    // 해시태그 등록
    @PostMapping("/hashtags")
    public ResponseEntity<DefaultRes> registerHashtags(@RequestBody final HashtagReq hashtagReq) {
        try{
            return new ResponseEntity<>(hashtagService.saveHashtags(hashtagReq), HttpStatus.OK);
        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }

    // 해시태그 조회
    @GetMapping("/hashtags")
    public ResponseEntity<DefaultRes> findHashtag(@RequestParam final String tag) {
        try{
            return new ResponseEntity<>(hashtagService.getHashtags(tag), HttpStatus.OK);
        } catch (Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.NOT_FOUND);
        }
    }
}
