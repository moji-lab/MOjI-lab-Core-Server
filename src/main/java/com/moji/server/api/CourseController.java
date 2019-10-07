package com.moji.server.api;

import com.moji.server.model.BoardReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.service.CourseService;
import com.moji.server.util.auth.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course/{courseIdx}")
    public ResponseEntity getCourse(@PathVariable(value = "courseIdx") final String courseIdx,
                                    final HttpServletRequest httpServletRequest) {
        try {
            return new ResponseEntity<>(courseService.getCourse(courseIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Auth
    @PutMapping("/courses/{courseIdx}/photo/{photoIdx}/public")
    public ResponseEntity isOpenPhoto(@PathVariable(value = "courseIdx") final String courseIdx,
                                      @PathVariable(value = "photoIdx") final int photoIdx,
                                      final HttpServletRequest httpServletRequest) {
        try {
            final int userIdx = (int) httpServletRequest.getAttribute("userIdx");
            return new ResponseEntity<>(courseService.isOpenPhoto(courseIdx, photoIdx, userIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //코스 등록
    public ResponseEntity<DefaultRes> saveCourse(@RequestBody final BoardReq board) {
        try {
            return new ResponseEntity<>(courseService.saveCourse(board), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //코스 공유
    public ResponseEntity<DefaultRes> shareCourse(@RequestBody final BoardReq board) {
        try {

            log.info("공유 = " + board.getCourses().toString());
            return new ResponseEntity<>(courseService.shareCourse(board), HttpStatus.OK);

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(DefaultRes.FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}