package com.moji.server.service;

import com.moji.server.domain.LikeBoard;
import com.moji.server.domain.LikeCourse;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.LikeReq;
import com.moji.server.repository.LikeBoardRepository;
import com.moji.server.repository.LikeCourseRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LikeService {
    private final BoardService boardService;
    private final CourseService courseService;
    private final LikeCourseRepository likeCourseRepository;
    private final LikeBoardRepository likeBoardRepository;

    public LikeService(
            @Lazy final BoardService boardService,
            @Lazy final CourseService courseService,
            final LikeCourseRepository likeCourseRepository,
            final LikeBoardRepository likeBoardRepository) {
        this.boardService = boardService;
        this.courseService = courseService;
        this.likeCourseRepository = likeCourseRepository;
        this.likeBoardRepository = likeBoardRepository;
    }

    /**
     * 보드 좋아요 / 좋아요 취소
     * 기존 좋아요 여부를 체크하여 기존에 좋아요를 했으면 삭제, 하지 않았으면 추가하는 로직
     * @param likeReq
     * @return
     */
    public DefaultRes checkBoardLike(LikeReq likeReq) {
        try {
            if (!boardService.isExistBoard(likeReq.getPostIdx())) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_BOARD);
            }

            LikeBoard savedLikeBoard = likeBoardRepository.findByBoardIdxAndUserIdx(likeReq.getPostIdx(), likeReq.getUserIdx());
            String responseMessage = ResponseMessage.SUCCESS_LIKE;

            // 기존에 좋아요하지 않은 경우
            if (savedLikeBoard == null) {
                LikeBoard likeBoard = new LikeBoard();
                likeBoard.setBoardIdx(likeReq.getPostIdx());
                likeBoard.setUserIdx(likeReq.getUserIdx());
                likeBoardRepository.save(likeBoard);
            } else { // 기존에 좋아요를 한 경우
                responseMessage = ResponseMessage.SUCCESS_UNLIKE;
                likeBoardRepository.delete(savedLikeBoard);
            }
            return DefaultRes.res(StatusCode.CREATED, responseMessage);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 코스 좋아요 / 좋아요 취소
     * 기존 좋아요 여부를 체크하여 기존에 좋아요를 했으면 삭제, 하지 않았으면 추가하는 로직
     * @param likeReq
     * @return
     */
    public DefaultRes checkCourseLike(LikeReq likeReq) {
        try {
            if (!courseService.isExistCourse(likeReq.getPostIdx())) {
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COURSE);
            }

            LikeCourse savedLikeCourse = likeCourseRepository.findByCourseIdxAndUserIdx(likeReq.getPostIdx(), likeReq.getUserIdx());
            String responseMessage = ResponseMessage.SUCCESS_LIKE;

            // 기존에 좋아요하지 않은 경우
            if (savedLikeCourse == null) {
                LikeCourse likeCourse = new LikeCourse();
                likeCourse.setCourseIdx(likeReq.getPostIdx());
                likeCourse.setUserIdx(likeReq.getUserIdx());
                likeCourseRepository.save(likeCourse);
            } else { // 기존에 좋아요를 한 경우
                responseMessage = ResponseMessage.SUCCESS_UNLIKE;
                likeCourseRepository.delete(savedLikeCourse);
            }
            return DefaultRes.res(StatusCode.CREATED, responseMessage);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public int getBoardLikeCount(String boardIdx) {
        return likeBoardRepository.countByBoardIdx(boardIdx);
    }
    public int getCourseLikeCount(String courseIdx) {
        return likeCourseRepository.countByCourseIdx(courseIdx);
    }

    public boolean isLikedBoard(String boardIdx, int userIdx) {
        return likeBoardRepository.findByBoardIdxAndUserIdx(boardIdx, userIdx) != null;
    }
    public boolean isLikedCourse(String courseIdx, int userIdx) {
        return likeCourseRepository.findByCourseIdxAndUserIdx(courseIdx, userIdx) != null;
    }
}
