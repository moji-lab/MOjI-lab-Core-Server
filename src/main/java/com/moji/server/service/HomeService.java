package com.moji.server.service;

import com.moji.server.domain.*;
import com.moji.server.domain.SearchResult.BoardSearchResult;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SearchRes.SearchBoardRes;
import com.moji.server.repository.*;
import com.moji.server.util.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {
    private final CourseService courseService;
    private final BoardRepository boardRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagCourseRepository hashtagCourseRepository;
    private final UserRepository userRepository;
    private final LikeBoardRepository likeBoardRepository;

    public HomeService(final CourseService courseService,
                       final BoardRepository boardRepository,
                       final HashtagRepository hashtagRepository,
                       final HashtagCourseRepository hashtagCourseRepository,
                       final UserRepository userRepository,
                       final LikeBoardRepository likeBoardRepository){
        this.courseService = courseService;
        this.boardRepository = boardRepository;
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.userRepository = userRepository;
        this.likeBoardRepository = likeBoardRepository;
    }

    // 고정된 키워드에 해당하는 기록하기 조회
    public DefaultRes getBoardsByFixedKeywords(final List<String> keywords) {
        for (int i = 0; i < keywords.size(); i++) {
            Optional<Hashtag> hashtag = hashtagRepository.findByTagInfo(keywords.get(i));
            if (hashtag.isPresent()) {
                List<String> courseIdxList = new ArrayList<>();
                List<HashtagCourse> hashtagCourses =
                        hashtagCourseRepository.findAllBytagIdx(hashtag.get().get_id()).get();
                for (int j = 0; j < hashtagCourses.size(); j++) {
                    courseIdxList.add(hashtagCourses.get(j).getCourseIdx());
                }
                List<String> boardIdxList = new ArrayList<>();
                for (String courseIdx : courseIdxList) {
                    boardIdxList.add(courseService.getCourse(courseIdx).getBoardIdx());
                }
                List<BoardSearchResult> boards = new ArrayList<>();
                for (int k = 0; k < boardIdxList.size(); k++) {
                    BoardSearchResult boardSearchResult = new BoardSearchResult();
                    Board board = boardRepository.findBy_id(boardIdxList.get(k));
                    boardSearchResult.setBoard(board);
                    boardSearchResult.setWriter(userRepository.findByUserIdx(board.getUserIdx()));
                    boardSearchResult.setLikeCount(likeBoardRepository.countByBoardIdx(board.get_id()));
                    boards.add(boardSearchResult);
                }
                if (boards.size() == 0) return DefaultRes.res(StatusCode.NOT_FOUND, "해당 키워드의 기록하기가 없습니다.");
                else {
                    Collections.sort(boards);
                    SearchBoardRes searchBoardRes = new SearchBoardRes(boards);
                    return DefaultRes.res(StatusCode.OK, "조회 성공", searchBoardRes);
                }
            } else {
                return DefaultRes.res(StatusCode.NOT_FOUND, "조회 실패");
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, "조회 실패");
    }
}
