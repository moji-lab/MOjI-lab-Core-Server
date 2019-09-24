package com.moji.server.service;

import com.moji.server.api.CourseController;
import com.moji.server.domain.*;
import com.moji.server.model.*;
import com.moji.server.repository.BoardRepository;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CourseController courseController;
    private final CourseService courseService;
    private final LikeService likeService;

    //생성자 의존성 주입
    public BoardService(final BoardRepository boardRepository,
                        final CourseController courseController,
                        final CourseService courseService,
                        final LikeService likeService,
                        final UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.courseController = courseController;
        this.courseService = courseService;
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    //게시물 작성
    @Transactional
    public DefaultRes saveBoard(final BoardReq board, int userIdx) {
        try {

            board.getInfo().setWriteTime(new Date());
            board.getInfo().setUserIdx(userIdx);
            boardRepository.save(board.getInfo());
            courseController.saveCourse(board);

            //공유
            for (int i = 0; i < board.getInfo().getShare().size(); i++) {
                Board info = board.getInfo();

                board.getInfo().setUserIdx(info.getShare().get(i));
                board.getInfo().setOpen(false);

                boardRepository.save(board.getInfo());
                courseController.saveCourse(board);
            }

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public Board getBoard(String boardIdx) {
        return boardRepository.findBy_id(boardIdx);
    }

    public void saveComment(String boardIdx, Comment comment) {
        Board board = getBoard(boardIdx);

        log.info(board.toString());

        List<Comment> commentList = board.getComments();
        commentList.add(comment);

        board.setComments(commentList);

        boardRepository.save(board);
    }

    public boolean isExistBoard(String postIdx) {
        return boardRepository.findBy_id(postIdx) != null;
    }

    //게시물 공유 사람 조회
    public DefaultRes getSharePerson(final String person) {
        try {
            Optional<User> email = userRepository.findByEmail(person);
            Optional<User> nickname = userRepository.findByNickname(person);
            Optional<PersonRes> personRes = Optional.of(new PersonRes());


            if (!email.isPresent() && !nickname.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, "해당 사용자 없음");
            else if (email.isPresent()) {
                personRes.get().setEmail(email.get().getEmail());
                personRes.get().setNickname(email.get().getNickname());
                personRes.get().setUserIdx(email.get().getUserIdx());
            }
            else
            {
                personRes.get().setEmail(nickname.get().getEmail());
                personRes.get().setNickname(nickname.get().getNickname());
                personRes.get().setUserIdx(nickname.get().getUserIdx());
            }

            return DefaultRes.res(StatusCode.OK, "사용자 조회 완료", personRes);

        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes getRecentFeed(int userIdx) {
        try {
            // TODO: 공개 대상인지 판단하여 공개 설정
            List<Board> boardList = boardRepository.findByOpenOrderByWriteTimeDesc(true); // TODO: 값 조정될 필요성

            List<FeedRes> feedResList = new ArrayList<>();
            for (int i = 0; i < boardList.size(); i++) {
                Board board = boardList.get(i);
                User user = userRepository.findByUserIdx(board.getUserIdx());
                if (user == null) {
                    continue;
                }

                List<Course> courseList = courseService.getFirstRepresentPhotoByBoardIdx(board.get_id());
                log.info(courseList.toString());
                if (courseList.size() == 0) { // 코스 정보가 없을 경우?
                    continue;
                }
                List<Photo> photoList = new ArrayList<>();

                for (int j = 0; j < courseList.size(); j++) {
                    photoList.add(courseList.get(j).getPhotos().get(0));
                }

                FeedRes feedRes = new FeedRes();
                feedRes.setNickName(user.getNickname()); // TODO: 탈퇴한 회원일 경우? 일단 그거빼고 게시물 보여줘...?
                feedRes.setProfileUrl(user.getPhotoUrl());
                feedRes.setBoardIdx(board.get_id());
                feedRes.setPlace(board.getSubAddress());
                feedRes.setPhotoList(photoList);
                feedRes.setDate(board.getWriteTime());
                feedRes.setCommentCount(board.getComments().size());
                feedRes.setLikeCount(likeService.getBoardLikeCount(board.get_id()));
                feedRes.setLiked(likeService.isLikedBoard(board.get_id(), userIdx));
                feedResList.add(feedRes);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FEED, feedResList);
        } catch (Exception e) {
            e.printStackTrace();
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public DefaultRes<BoardRes> getBoardInfo(String boardIdx, int userIdx) {
        try {
            BoardRes boardRes = new BoardRes();
            User user = userRepository.findByUserIdx(userIdx);
            Board board = boardRepository.findBy_id(boardIdx);

            if (board == null) {
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_BOARD);
            }

            boardRes.setUser(user);
            boardRes.set_id(boardIdx);
            boardRes.setWriteTime(board.getWriteTime());
            boardRes.setCourseList(courseService.getCourseListByBoardIdx(boardIdx, userIdx));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BOARD, boardRes);
        } catch (Exception e) {
            e.printStackTrace();
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
