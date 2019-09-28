package com.moji.server.service;

import com.moji.server.api.CourseController;
import com.moji.server.domain.*;
import com.moji.server.model.*;
import com.moji.server.repository.BoardRepository;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final ScrapService scrapService;

    //생성자 의존성 주입
    public BoardService(final BoardRepository boardRepository,
                        final CourseController courseController,
                        final CourseService courseService,
                        final LikeService likeService,
                        final UserRepository userRepository,
                        final ScrapService scrapService) {
        this.boardRepository = boardRepository;
        this.courseController = courseController;
        this.courseService = courseService;
        this.likeService = likeService;
        this.userRepository = userRepository;
        this.scrapService = scrapService;
    }

    //게시물 작성
    @Transactional
    public DefaultRes saveBoard(final BoardReq board, int userIdx) throws CloneNotSupportedException{
        try {
            BoardRes2 data = BoardRes2.getBoardRes2();
            data.getCourseIdx().clear();

            board.getInfo().setWriteTime(LocalDate.now());
            board.getInfo().setUserIdx(userIdx);


            //DB에 저장
            boardRepository.save(board.getInfo());
            courseController.saveCourse(board);

            //공유
            for (int i = 0; i < board.getInfo().getShare().size(); i++) {

                BoardReq info = (BoardReq) board.clone();
                info.getInfo().set_id(null);

                for(int s = 0; s < board.getCourses().size(); s++) {
                    for (int j = 0; j < board.getCourses().get(s).getPhotos().size(); j++) {
                        info.getCourses().get(s).getPhotos().get(j).setPhoto(null);
                        info.getCourses().get(s).getPhotos().get(j).setPhotoUrl(board.getCourses().get(s).getPhotos().get(j).getPhotoUrl());
                    }
                }
                info.getInfo().getShare().clear();
                info.getInfo().setOpen(false);
                info.getInfo().setUserIdx(board.getInfo().getShare().get(i));


                boardRepository.save(info.getInfo());
                log.info("---------------------");
                courseController.shareCourse(info);

            }

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD,data);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.FAIL_CREATE_BOARD);
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
                personRes.get().setPhotoUrl(email.get().getPhotoUrl());
            } else {
                personRes.get().setEmail(nickname.get().getEmail());
                personRes.get().setNickname(nickname.get().getNickname());
                personRes.get().setUserIdx(nickname.get().getUserIdx());
                personRes.get().setPhotoUrl(nickname.get().getPhotoUrl());
            }

            return DefaultRes.res(StatusCode.OK, "사용자 조회 완료", personRes);

        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes getRecentFeed(final int userIdx) {
        return getDefault(userIdx, boardRepository.findByOpenOrderByWriteTimeDesc(true));
    }

    public DefaultRes getBoardList(final int userIdx) {
        return getDefault(userIdx, boardRepository.findByUserIdx(userIdx));
    }

    public DefaultRes getScrapList(final int userIdx, final List<Board> list) {
        return getDefault(userIdx, list);
    }

    private DefaultRes getDefault(final int userIdx, final List<Board> boardList) {
        try {
            List<FeedRes> feedResList = new ArrayList<>();
            for (Board board : boardList) {
                Optional<User> user = userRepository.findByUserIdx(board.getUserIdx());

                if (!user.isPresent()) {
                    continue;
                }

                List<Course> courseList = courseService.getFirstRepresentPhotoByBoardIdx(board.get_id());
                if (courseList.size() == 0) { // 코스 정보가 없을 경우?
                    continue;
                }
                List<Photo> photoList = new ArrayList<>();

                for (int j = 0; j < courseList.size(); j++) {
                    if(courseList.get(j).getPhotos().get(0) != null) {
                        photoList.add(courseList.get(j).getPhotos().get(0));
                    }
                }

                FeedRes feedRes = new FeedRes();
                feedRes.setNickName(user.get().getNickname()); // TODO: 탈퇴한 회원일 경우? 일단 그거빼고 게시물 보여줘...?
                feedRes.setProfileUrl(user.get().getPhotoUrl());
                feedRes.setBoardIdx(board.get_id());
                feedRes.setPlace(board.getSubAddress());
                feedRes.setComments(board.getComments());
                feedRes.setPhotoList(photoList);
                feedRes.setDate(board.getWriteTime());
                feedRes.setCommentCount(board.getComments().size());
                feedRes.setLikeCount(likeService.getBoardLikeCount(board.get_id()));
                feedRes.setLiked(likeService.isLikedBoard(board.get_id(), userIdx));
                feedRes.setMainAddress(board.getMainAddress());
                feedRes.setScraped(scrapService.isScraped(userIdx, board.get_id()));
                feedResList.add(feedRes);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FEED, feedResList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public DefaultRes<BoardRes> getBoardInfo(final String boardIdx, final int userIdx) {
        try {
            final BoardRes boardRes = new BoardRes();
            final Optional<User> user = userRepository.findByUserIdx(userIdx);
            final Board board = boardRepository.findBy_id(boardIdx);

            if (board == null) {
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_BOARD);
            }

            boardRes.setUser(user.get());
            boardRes.set_id(boardIdx);
            boardRes.setWriteTime(board.getWriteTime());
            boardRes.setCourseList(courseService.getCourseListByBoardIdx(boardIdx, userIdx));
            boardRes.setScraped(scrapService.isScraped(userIdx, boardIdx));
            boardRes.setLiked(likeService.isLikedBoard(board.get_id(), userIdx));
            boardRes.setLikeCount(likeService.getBoardLikeCount(board.get_id()));
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BOARD, boardRes);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
