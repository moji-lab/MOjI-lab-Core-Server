package com.moji.server.api;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.ScrapReq;
import com.moji.server.service.ScrapService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By ds on 25/09/2019.
 */

@Slf4j
@RestController
public class ScrapController {

    private final ScrapService scrapService;

    public ScrapController(final ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    @Auth
    @PostMapping("/scrap")
    public ResponseEntity<DefaultRes> scrap(
            final HttpServletRequest httpServletRequest,
            @RequestBody final ScrapReq scrapReq) {
        try {
            scrapReq.setUserIdx((int) httpServletRequest.getAttribute("userIdx"));
            return new ResponseEntity<>(scrapService.scrap(scrapReq), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @DeleteMapping("/scrap")
    public ResponseEntity<DefaultRes> deleteScrap(
            final HttpServletRequest httpServletRequest,
            @RequestBody final ScrapReq scrapReq) {
        try {
            scrapReq.setUserIdx((int) httpServletRequest.getAttribute("userIdx"));
            return new ResponseEntity<>(scrapService.deleteScrap(scrapReq), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}