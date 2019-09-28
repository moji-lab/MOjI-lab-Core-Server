package com.moji.server.service;

import com.moji.server.domain.*;
import com.moji.server.domain.SearchResult.CourseSearchResult;
import com.moji.server.model.CourseRes;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.HomeRes;
import com.moji.server.model.SearchRes;
import com.moji.server.model.SearchRes.SearchCourseRes;
import com.moji.server.repository.*;
import com.moji.server.util.StatusCode;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeService {
    private final CourseRepository courseRepository;
    private final HashtagRepository hashtagRepository;
    private final HashtagCourseRepository hashtagCourseRepository;
    private final UserRepository userRepository;
    private final LikeCourseRepository likeCourseRepository;
    private final CourseService courseService;
    private final LikeService likeService;

    public HomeService(final CourseRepository courseRepository,
                       final HashtagRepository hashtagRepository,
                       final HashtagCourseRepository hashtagCourseRepository,
                       final UserRepository userRepository,
                       final LikeCourseRepository likeCourseRepository,
                       final CourseService courseService,
                       final LikeService likeService) {
        this.courseRepository = courseRepository;
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.userRepository = userRepository;
        this.likeCourseRepository = likeCourseRepository;
        this.courseService = courseService;
        this.likeService = likeService;
    }

    // 홈 화면 데이터 조회
    public DefaultRes getHomeData(final int userIdx,
                                  final String hotCategoryKeyword,
                                  final List<String> hotKeywords,
                                  final List<String> recommendKeywords){
        try{
            HomeRes homeRes = new HomeRes();
            homeRes.setNickName(userRepository.findByUserIdx(userIdx).get().getNickname());
            homeRes.setHotCategoryKeyword(hotCategoryKeyword);
            homeRes.setHotKeywords(hotKeywords); homeRes.setRecommendKeywords(recommendKeywords);
            homeRes.setTopKeywords(this.orderByCount(hashtagCourseRepository.findAll()));
            return DefaultRes.res(StatusCode.OK, "홈 조회 성공", homeRes);
        }
        catch(Exception e){
            return DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러");
        }
    }


    public List<String> orderByCount(List<HashtagCourse> hashtagCourses){
        final Map<String, Integer> counter = new HashMap<String, Integer>();
        for (HashtagCourse hashtagCourse : hashtagCourses)
            counter.put(hashtagCourse.getTagIdx(), 1 +
                    (counter.containsKey(hashtagCourse.getTagIdx()) ? counter.get(hashtagCourse.getTagIdx()) : 0));
        List<String> list = new ArrayList<>(counter.keySet());
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String x, String y) {
                return counter.get(y) - counter.get(x);
            }
        });
        list = list.subList(0,5);
        List<String> tagInfoList = new ArrayList<>();
        for (String tagIdx : list) {
            tagInfoList.add(hashtagRepository.findById(tagIdx).get().getTagInfo());
        }
        return tagInfoList;
    }
}




