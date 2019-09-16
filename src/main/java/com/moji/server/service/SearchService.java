package com.moji.server.service;

import com.moji.server.domain.*;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SearchRes.SearchCourseRes;
import com.moji.server.model.SearchRes.SearchBoardRes;
import com.moji.server.domain.SearchResult.BoardSearchResult;
import com.moji.server.domain.SearchResult.CourseSearchResult;
import com.moji.server.repository.*;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SearchService {
    private final HashtagRepository hashtagRepository;
    private final HashtagCourseRepository hashtagCourseRepository;
    private final SearchCourseRepository searchCourseRepository;
    private final AddressRepository addressRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final LikeCourseRepository likeCourseRepository;

    // 생성자 의존성 주입
    public SearchService(final HashtagRepository hashtagRepository,
                         final HashtagCourseRepository hashtagCourseRepository,
                         final SearchCourseRepository searchCourseRepository,
                         final AddressRepository addressRepository,
                         final BoardRepository boardRepository,
                         final UserRepository userRepository,
                         final LikeBoardRepository likeBoardRepository,
                         final LikeCourseRepository likeCourseRepository) {
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.searchCourseRepository = searchCourseRepository;
        this.addressRepository = addressRepository;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.likeBoardRepository = likeBoardRepository;
        this.likeCourseRepository = likeCourseRepository;
    }

    // 해시태그 키워드로 검색 결과 조회
    // 가 데이터 쌓이면 조회 총 개수 5개 이상일 경우만 검색 결과 노출하기 구현
    public DefaultRes getSearchResultByHashtag(final String keyword){
        Optional<List<Hashtag>> hashtags = hashtagRepository.findAllByTagInfoContaining(keyword);
        if(hashtags.isPresent()){
            List<CourseSearchResult> courses = new ArrayList<>();
            for(int i = 0; i<hashtags.get().size(); i++){
                List<HashtagCourse> hashtagCourses =
                        hashtagCourseRepository.findAllBytagIdx(hashtags.get().get(i).get_id()).get();
                for(int j = 0; j<hashtagCourses.size(); j++){
                    String courseIdx = hashtagCourses.get(j).getCourseIdx();
                    Course course = searchCourseRepository.findBy_id(courseIdx).get();
                    CourseSearchResult courseSearchResult = new CourseSearchResult();
                    courseSearchResult.setCourse(course);
                    courseSearchResult.setWriter(userRepository.findByUserIdx(course.getUserIdx()));
                    courseSearchResult.setLikeCount(likeCourseRepository.countByCourseIdx(course.get_id()));
                    courses.add(courseSearchResult);
                }
            }
            if(courses.size() == 0) return DefaultRes.res(StatusCode.NOT_FOUND, "검색된 데이터가 없습니다.");
            else {
                Collections.sort(courses);
                SearchCourseRes searchCourseRes = new SearchCourseRes(courses);
                return DefaultRes.res(StatusCode.OK, "검색 성공",searchCourseRes);
            }
        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, "검색 실패");
        }
    }

    // 장소 or 주소 키워드로 검색 결과 조회
    // 가 데이터 쌓이면 조회 총 개수 5개 이상일 경우만 검색 결과 노출하기 구현
    public DefaultRes getSearchResultByPlace(final String keyword){
        Optional<List<Address>> addresses = addressRepository.findAllByPlaceContaining(keyword);
        if(addresses.isPresent()){
            List<BoardSearchResult> boards = new ArrayList<>();
            for(int i = 0; i<addresses.get().size(); i++){
                BoardSearchResult boardSearchResult = new BoardSearchResult();
                String boardIdx = addresses.get().get(i).getBoardIdx();
                Board board =  boardRepository.findBy_id(boardIdx);
                boardSearchResult.setBoard(board);
                boardSearchResult.setWriter(userRepository.findByUserIdx(board.getUserIdx()));
                boardSearchResult.setLikeCount(likeBoardRepository.countByBoardIdx(board.get_id()));
                boards.add(boardSearchResult);
            }
            if(boards.size() == 0) return DefaultRes.res(StatusCode.NOT_FOUND, "검색된 데이터가 없습니다.");
            else {
                Collections.sort(boards);
                SearchBoardRes searchBoardRes = new SearchBoardRes(boards);
                return DefaultRes.res(StatusCode.OK, "검색 성공", searchBoardRes);
            }
        }
        else{
            return DefaultRes.res(StatusCode.NOT_FOUND, "검색 실패");
        }

    }
}