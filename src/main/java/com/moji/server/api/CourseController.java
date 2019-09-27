package com.moji.server.api;

import com.moji.server.model.BoardReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }

    //코스 등록
    @PostMapping("course")
    public ResponseEntity<DefaultRes> saveCourse(@RequestBody final BoardReq board) {
        try {

            return new ResponseEntity<>(courseService.saveCourse(board), HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}