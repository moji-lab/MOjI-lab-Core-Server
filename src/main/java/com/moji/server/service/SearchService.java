package com.moji.server.service;

import com.moji.server.domain.*;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SearchReq;
import com.moji.server.model.SearchRes.SearchPlaceRes;
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
    private final CourseRepository courseRepository;
    private final AddressRepository addressRepository;
    private final BoardRepository boardRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final LikeCourseRepository likeCourseRepository;
    private final CourseService courseService;
    private final LikeService likeService;
    private final UserRepository userRepository;

    // 생성자 의존성 주입
    public SearchService(final HashtagRepository hashtagRepository,
                         final HashtagCourseRepository hashtagCourseRepository,
                         final CourseRepository courseRepository,
                         final AddressRepository addressRepository,
                         final BoardRepository boardRepository,
                         final LikeBoardRepository likeBoardRepository,
                         final LikeCourseRepository likeCourseRepository,
                         final CourseService courseService,
                         final LikeService likeService,
                         final UserRepository userRepository) {
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.courseRepository = courseRepository;
        this.addressRepository = addressRepository;
        this.boardRepository = boardRepository;
        this.likeBoardRepository = likeBoardRepository;
        this.likeCourseRepository = likeCourseRepository;
        this.courseService = courseService;
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    // 해시태그 키워드로 검색 결과 조회
    // 가 데이터 쌓이면 조회 총 개수 5개 이상일 경우만 검색 결과 노출하기 구현
    public DefaultRes getSearchResultByHashtag(final SearchReq searchReq){
        Optional<List<Hashtag>> hashtags = hashtagRepository.findAllByTagInfoContaining(searchReq.getKeyword());
        if(hashtags.isPresent()){
            List<CourseSearchResult> courses = new ArrayList<>();
            for(int i = 0; i<hashtags.get().size(); i++){
                List<HashtagCourse> hashtagCourses =
                        hashtagCourseRepository.findAllBytagIdx(hashtags.get().get(i).get_id()).get();
                for(int j = 0; j<hashtagCourses.size(); j++){
                    String courseIdx = hashtagCourses.get(j).getCourseIdx();
                    Course course;

                    // 날짜 필터 적용시
                    if(searchReq.getStartDate() != null && searchReq.getEndDate() != null){
                        course = courseRepository.findBy_idAndVisitTimeBetween(courseIdx,
                                searchReq.getStartDate(),
                                searchReq.getEndDate());
                    }
                    // 날짜 필터 미적용시
                    else{
                        course = courseRepository.findBy_id(courseIdx);
                    }
                    CourseSearchResult courseSearchResult = new CourseSearchResult();
                    courseSearchResult.setCourse(course);
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
    public DefaultRes getSearchResultByPlace(final SearchReq searchReq, final int userIdx){
        try{
            Optional<SearchBoardRes> searchBoardRes  = this.getSearchBoardRes(searchReq, userIdx);
            Optional<SearchCourseRes> searchCourseRes = this.getSearchCourseRes(searchReq, userIdx);
            SearchPlaceRes searchPlaceRes = new SearchPlaceRes(searchBoardRes.get(), searchCourseRes.get());
            return DefaultRes.res(StatusCode.OK, "검색 성공", searchPlaceRes);
        }
        catch(Exception e){
            return DefaultRes.res(StatusCode.NOT_FOUND, "검색 실패");
        }
    }

    public Optional<SearchBoardRes> getSearchBoardRes(final SearchReq searchReq, final int userIdx) {
        SearchBoardRes searchBoardRes = new SearchBoardRes();
        Optional<List<Address>> addresses = addressRepository.findAllByPlaceContaining(searchReq.getKeyword());
        if(addresses.isPresent()) {
            List<BoardSearchResult> boards = new ArrayList<>();
            for (int i = 0; i < addresses.get().size(); i++) {
                String boardIdx = addresses.get().get(i).getBoardIdx();
                Board board;
                if (searchReq.getStartDate() != null && searchReq.getEndDate() != null) {
                    board = boardRepository.findBy_idAndWriteTimeBetween(boardIdx,
                            searchReq.getStartDate(),
                            searchReq.getEndDate());
                } else {
                    board = boardRepository.findBy_id(boardIdx);
                }

                Optional<User> user = userRepository.findByUserIdx(board.getUserIdx());

                if (!user.isPresent()) continue;

                List<Course> courseList = courseService.getFirstRepresentPhotoByBoardIdx(board.get_id());
                log.info(courseList.toString());
                if (courseList.size() == 0) { // 코스 정보가 없을 경우?
                    continue;
                }
                List<Photo> photoList = new ArrayList<>();

                for (int j = 0; j < courseList.size(); j++) {
                    photoList.add(courseList.get(j).getPhotos().get(0));
                }

                BoardSearchResult boardSearchResult = new BoardSearchResult();
                boardSearchResult.setNickName(user.get().getNickname());
                boardSearchResult.setProfileUrl(user.get().getPhotoUrl());
                boardSearchResult.setBoardIdx(board.get_id());
                boardSearchResult.setPlace(board.getSubAddress());
                boardSearchResult.setPhotoList(photoList);

                boardSearchResult.setDate(board.getWriteTime());
                boardSearchResult.setCommentCount(board.getComments().size());
                boardSearchResult.setLikeCount(likeService.getBoardLikeCount(board.get_id()));
                boardSearchResult.setLiked(likeService.isLikedBoard(board.get_id(), userIdx));
                boards.add(boardSearchResult);
                boards.add(boardSearchResult);
            }
                Collections.sort(boards);
                searchBoardRes = new SearchBoardRes(boards);
            }
        return Optional.ofNullable(searchBoardRes);
    }

    public Optional<SearchCourseRes> getSearchCourseRes(final SearchReq searchReq, final int userIdx) {
        SearchCourseRes searchCourseRes = new SearchCourseRes();

        Optional<List<Course>> coursesByMainAddress = courseRepository.findAllByMainAddressContaining(searchReq.getKeyword());
        Optional<List<Course>> coursesBySubAddress = courseRepository.findAllBySubAddressContaining(searchReq.getKeyword());
        if(coursesByMainAddress.isPresent() || coursesBySubAddress.isPresent()) {
            List<CourseSearchResult> courseSearchResultList = new ArrayList<>();
            Optional<List<Course>> temp;
            for(int k = 0; k < 2; k++) {
                if(k == 0) temp = coursesByMainAddress;
                else temp = coursesBySubAddress;
                for (int i = 0; i < temp.get().size(); i++) {
                    String courseIdx = temp.get().get(i).get_id();
                    Course course;

                    // 날짜 필터 적용시
                    if (searchReq.getStartDate() != null && searchReq.getEndDate() != null) {
                        course = courseRepository.findBy_idAndVisitTimeBetween(courseIdx,
                                searchReq.getStartDate(),
                                searchReq.getEndDate());
                    }
                    // 날짜 필터 미적용시
                    else {
                        course = courseRepository.findBy_id(courseIdx);
                    }
                    CourseSearchResult courseSearchResult = new CourseSearchResult();
                    courseSearchResult.setCourse(course);
                    courseSearchResult.setLikeCount(likeCourseRepository.countByCourseIdx(course.get_id()));
                    courseSearchResultList.add(courseSearchResult);
                }
            }
            Collections.sort(courseSearchResultList);
            searchCourseRes.setCourses(courseSearchResultList);
        }
        return Optional.ofNullable(searchCourseRes);
    }
}