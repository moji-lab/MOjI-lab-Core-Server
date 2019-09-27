package com.moji.server.service;

import com.moji.server.domain.*;
import com.moji.server.domain.SearchResult.CourseSearchResult;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SearchRes.SearchCourseRes;
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
    private final CourseRepository courseRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagCourseRepository hashtagCourseRepository;
    private final UserRepository userRepository;
    private final LikeCourseRepository likeCourseRepository;

    public HomeService(final CourseService courseService,
                       final CourseRepository courseRepository,
                       final HashtagRepository hashtagRepository,
                       final HashtagCourseRepository hashtagCourseRepository,
                       final UserRepository userRepository,
                       final LikeCourseRepository likeCourseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.userRepository = userRepository;
        this.likeCourseRepository = likeCourseRepository;
    }

    // 고정된 키워드에 해당하는 코스 조회
    public DefaultRes getCoursesByFixedKeywords(final List<String> keywords) {
        try {
            List<SearchCourseRes> searchCourseResList = new ArrayList<>();
            for (int i = 0; i < keywords.size(); i++) {
                Optional<Hashtag> hashtag = hashtagRepository.findByTagInfo(keywords.get(i));
                if (hashtag.isPresent()) {
                    List<String> courseIdxList = new ArrayList<>();
                    List<HashtagCourse> hashtagCourses =
                            hashtagCourseRepository.findAllBytagIdx(hashtag.get().get_id()).get();
                    for (int j = 0; j < hashtagCourses.size(); j++) {
                        courseIdxList.add(hashtagCourses.get(j).getCourseIdx());
                    }
                    List<CourseSearchResult> courses = new ArrayList<>();
                    for (int k = 0; k < courseIdxList.size(); k++) {
                        CourseSearchResult courseSearchResult = new CourseSearchResult();
                        Course course = courseRepository.findBy_id(courseIdxList.get(k));
                        courseSearchResult.setCourse(course);
                        courseSearchResult.setWriter(userRepository.findByUserIdx(course.getUserIdx()).get());
                        courseSearchResult.setLikeCount(likeCourseRepository.countByCourseIdx(course.get_id()));
                        courses.add(courseSearchResult);
                    }
                    if (courses.size() == 0) return DefaultRes.res(StatusCode.NOT_FOUND, "해당 키워드의 코스가 없습니다.");
                    else {
                        Collections.sort(courses);
                        SearchCourseRes searchCourseRes = new SearchCourseRes(courses);
                        searchCourseResList.add(searchCourseRes);
                    }
                } else {
                    return DefaultRes.res(StatusCode.NOT_FOUND, "해당 키워드에 해당되는 해시태그가 등록되어 있지 않습니다.");
                }
            }
            return DefaultRes.res(StatusCode.OK, "키워드에 해당되는 코스 조회 성공", searchCourseResList);
        } catch (Exception e) {
            return DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러");
        }
    }
}


