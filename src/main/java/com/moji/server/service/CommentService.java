package com.moji.server.service;

import com.moji.server.domain.Board;
import com.moji.server.domain.Comment;
import com.moji.server.domain.Course;
import com.moji.server.model.BoardRes;
import com.moji.server.model.CommentReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.CourseRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommentService {
    private final BoardService boardService;
    private final CourseService courseService;

    public CommentService(
            final BoardService boardService,
            final CourseService courseService) {
        this.boardService = boardService;
        this.courseService = courseService;
    }

//    public DefaultRes saveBoardComment(final CommentReq commentReq) {
//        String boardIdx = boardService.findBoard(commentReq.getBoardIdx());
//
//        if (boardIdx == null) { // TODO: null인지 empty 인지 확인해 볼 것!
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD);
//        }
//
//        try {
//
//
//        } catch (Exception e) {
//
//        }
//    }

    /**
     * course comment 저장
     *
     * @param commentReq
     * @return
     */
    public DefaultRes saveCourseComment(CommentReq commentReq) {
        try {
            courseService.saveComment(commentReq.getPostIdx(), setComments(commentReq));
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_COURSE_COMMENT);
        } catch (NullPointerException e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COURSE);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * board comment 저장
     *
     * @param commentReq
     * @return
     */
    public DefaultRes saveBoardComment(CommentReq commentReq) {
        try {
            boardService.saveComment(commentReq.getPostIdx(), setComments(commentReq));
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD_COMMENT);
        } catch (NullPointerException e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private Comment setComments(CommentReq commentReq) {
        Comment comment = new Comment();
        comment.setContent(commentReq.getContent());
        comment.setUserIdx(commentReq.getUserIdx());
        comment.setWriteTime(new Date());

        return comment;
    }

    public DefaultRes getBoardComments(final int userIdx, final String boardIdx) {
        BoardRes boardRes = boardService.getBoardInfo(boardIdx, userIdx).getData();
        if (boardRes.getComments() != null) return DefaultRes.res(StatusCode.OK, "조회 성공", boardRes.getComments());
        return DefaultRes.NOT_FOUND;
    }

    public DefaultRes getCourseComments(final String courseIdx) {
        Course course = courseService.getCourse(courseIdx);
        if (course != null) return DefaultRes.res(StatusCode.OK, "조회 성공", course.getComments());
        return DefaultRes.NOT_FOUND;
    }
}
