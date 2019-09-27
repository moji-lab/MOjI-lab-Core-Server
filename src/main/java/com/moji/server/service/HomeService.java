package com.moji.server.service;

import com.moji.server.domain.*;
import com.moji.server.domain.SearchResult.CourseSearchResult;
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

    public HomeService(final CourseRepository courseRepository,
                       final HashtagRepository hashtagRepository,
                       final HashtagCourseRepository hashtagCourseRepository,
                       final UserRepository userRepository,
                       final LikeCourseRepository likeCourseRepository) {
        this.courseRepository = courseRepository;
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.userRepository = userRepository;
        this.likeCourseRepository = likeCourseRepository;
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

            Optional<List<SearchCourseRes>> hotKeywordSearchCourseResList = this.getCoursesByFixedKeywords(hotKeywords);
            Optional<List<SearchCourseRes>> recommendKeywordSearchCourseResList = this.getCoursesByFixedKeywords(recommendKeywords);
            if(hotKeywordSearchCourseResList.isPresent() &&
                    recommendKeywordSearchCourseResList.isPresent()){
                if(hotKeywordSearchCourseResList.get().size() == 0  ||
                        recommendKeywordSearchCourseResList.get().size() == 0){
                    return DefaultRes.res(StatusCode.NOT_FOUND, "홈 데이터가 없습니다.");
                }
            }
            homeRes.setHotCourseSearchCourseRes(hotKeywordSearchCourseResList.get());
            homeRes.setRecommendCourseSearchCourseRes(recommendKeywordSearchCourseResList.get());
            return DefaultRes.res(StatusCode.OK, "홈 조회 성공", homeRes);
        }
        catch(Exception e){
            return DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러");
        }
    }

    // 고정된 키워드에 해당하는 코스 조회
    public Optional<List<SearchCourseRes>> getCoursesByFixedKeywords(final List<String> keywords) {
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
                    courseSearchResult.setLikeCount(likeCourseRepository.countByCourseIdx(course.get_id()));
                    courses.add(courseSearchResult);
                }
                Collections.sort(courses);
                SearchCourseRes searchCourseRes = new SearchCourseRes(courses);
                searchCourseResList.add(searchCourseRes);
            }
        }
        return Optional.ofNullable(searchCourseResList);
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
        list.subList(0,5);
        List<String> tagInfoList = new ArrayList<>();
        for (String tagIdx : list) {
            tagInfoList.add(hashtagRepository.findById(tagIdx).get().getTagInfo());
        }
        return tagInfoList;
    }
}




