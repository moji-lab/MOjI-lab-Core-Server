package com.moji.server.service;

import com.moji.server.domain.Course;
import com.moji.server.domain.Scrap;
import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.MyPageRes;
import com.moji.server.repository.CourseRepository;
import com.moji.server.repository.ScrapRepository;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created By ds on 25/09/2019.
 */

@Slf4j
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ScrapRepository scrapRepository;

    public MyPageService(final UserRepository userRepository,
                         final CourseRepository courseRepository,
                         final ScrapRepository scrapRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.scrapRepository = scrapRepository;
    }

    /**
     * 마이페이지- 프로필사진, 닉네임 데이터 줄때 나의 기록 총 게시글 개수도 같이 보내줘
     * <p>
     * 마이 페이지 1 - 나의 기록하기 리스트(친구와 공유한 기록하기 리스트 포함)
     * 마이 페이지 2 - 스크랩 한 글 리스트
     */

    public DefaultRes getMyCourseList(final int userIdx) {
        defaultInfo defaultInfo = getMyPageDefaultInfo(userIdx);
        MyPageRes<Course> myPageRes = new MyPageRes<>();
        myPageRes.setNickname(defaultInfo.nickname);
        myPageRes.setProfileUrl(defaultInfo.profileUrl);
        myPageRes.setCourseCount(defaultInfo.courseList.size());
        myPageRes.setScrapCount(defaultInfo.scrapList.size());
        myPageRes.setFeedList(defaultInfo.courseList);
        return DefaultRes.res(StatusCode.OK, "조회 성공", myPageRes);
    }

    public DefaultRes getMyScrapCourseList(final int userIdx) {
        defaultInfo defaultInfo = getMyPageDefaultInfo(userIdx);
        MyPageRes<Course> myPageRes = new MyPageRes<>();
        myPageRes.setNickname(defaultInfo.nickname);
        myPageRes.setProfileUrl(defaultInfo.profileUrl);
        myPageRes.setCourseCount(defaultInfo.courseList.size());
        myPageRes.setScrapCount(defaultInfo.scrapList.size());

        List<Course> courseList = new LinkedList<>();

        for (Scrap s : defaultInfo.scrapList) {
            courseList.add(courseRepository.findBy_id(s.getBoardIdx()));
        }

        return DefaultRes.res(StatusCode.OK, "조회 성공", courseList);
    }

    //기본으로 프로필 사진, 닉네임, 나의 기록(친구와 공유), 스크랩 한 글 갯수 포함
    private defaultInfo getMyPageDefaultInfo(final int userIdx) {
        Optional<User> user = userRepository.findByUserIdx(userIdx);
        List<Scrap> scrapList = scrapRepository.findByUserIdx(userIdx);
        List<Course> courseList = courseRepository.findByUserIdx(userIdx);
        return new defaultInfo(user.get().getPhotoUrl(), user.get().getNickname(), courseList, scrapList);
    }

    private static class defaultInfo {
        private String profileUrl;
        private String nickname;
        private List<Course> courseList;
        private List<Scrap> scrapList;

        defaultInfo(final String profileUrl, final String nickname, final List<Course> courseList, final List<Scrap> scrapList) {
            this.profileUrl = profileUrl;
            this.nickname = nickname;
            this.courseList = courseList;
            this.scrapList = scrapList;
        }
    }
}