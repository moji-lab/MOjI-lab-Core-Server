package com.moji.server.service;

import com.moji.server.api.CourseController;
import com.moji.server.domain.*;
import com.moji.server.model.BoardReq;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.FeedRes;
import com.moji.server.repository.BoardRepository;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                        final UserRepository userRepository)
    {
        this.boardRepository = boardRepository;
        this.courseController = courseController;
        this.courseService = courseService;
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    //게시물 작성
    @Transactional
    public DefaultRes saveBoard(final BoardReq board)
    {
        try{

            board.getInfo().setWriteTime(new Date());
            boardRepository.save(board.getInfo());
            courseController.saveCourse(board);

            //공유
            for(int i = 0; i < board.getInfo().getShare().size(); i++)
            {
                Board info = board.getInfo();

                board.getInfo().setUserIdx(info.getShare().get(i));
                board.getInfo().setOpen(false);

                boardRepository.save(board.getInfo());
                courseController.saveCourse(board);
            }

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        }catch (Exception e)
        {
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

    //게시물 공유
    public DefaultRes shareBoard(final String person)
    {
        try{

            List<User> email = userRepository.findByEmail(person);
            List<User> nickname = userRepository.findByNickname(person);


            if(email.size() == 0 && nickname.size() == 0) return DefaultRes.res(StatusCode.NOT_FOUND, "해당 사용자 없음");
            else if(email.size() > 0) return DefaultRes.res(StatusCode.OK, "사용자 조회 완료", email);
            else return DefaultRes.res(StatusCode.OK, "사용자 조회 완료", nickname);

        } catch (Exception e)
        {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes getRecentFeed(int userIdx) {
        try {
            // TODO: 공개 대상인지 판단하여 공개 설정
            List<Board> boardList = boardRepository.findByOpen(true); // TODO: 값 조정될 필요성

            log.info(boardList.toString());
            List<FeedRes> feedResList = new ArrayList<>();
            for (int i = 0; i < boardList.size(); i++) {
                log.info("i " + i);
                Board board = boardList.get(i);
                List<Course> courseList = courseService.getAllRepresentPhotosByBoardIdx(board.get_id());
                List<Photo> photoList = new ArrayList<>();

                for (int j = 0; j < courseList.size(); j++) {
                    photoList.add(courseList.get(j).getPhotos().get(0));
                }
                User user = userRepository.findByUserIdx(board.getUserIdx());

                FeedRes feedRes = new FeedRes();

                feedRes.setNickName(user.getNickname());
                feedRes.setProfileUrl(user.getPhotoUrl());
                feedRes.setBoardIdx(board.get_id());
                feedRes.setPlace(board.getSubAddress());
                feedRes.setPhotoList(photoList);
                feedRes.setDate(board.getWriteTime());
                feedRes.setCommentCount(board.getComments().size());
                feedRes.setLikeCount(likeService.getBoardLikeCount(board.get_id()));

                feedResList.add(feedRes);
            }
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FEED, feedResList);
        } catch (Exception e) {
            e.printStackTrace();
            return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
