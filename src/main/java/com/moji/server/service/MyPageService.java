package com.moji.server.service;

import com.moji.server.domain.Course;
import com.moji.server.domain.Scrap;
import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.CourseRepository;
import com.moji.server.repository.ScrapRepository;
import com.moji.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        myInfo myInfo = getMyPageDefaultInfo(userIdx);
        return null;
    }

    public DefaultRes getMyScrapCourseList(final int userIdx) {
        myInfo myInfo = getMyPageDefaultInfo(userIdx);
        return null;
    }

    //기본으로 프로필 사진, 닉네임, 나의 기록(친구와 공유), 스크랩 한 글 갯수 포함
    private myInfo getMyPageDefaultInfo(final int userIdx) {
        Optional<User> user = userRepository.findByUserIdx(userIdx);
        List<Scrap> scrapList = scrapRepository.findByUserIdx(userIdx);
        List<Course> courseList = courseRepository.findByUserIdx(userIdx);
        return new myInfo(user.get().getPhotoUrl(), user.get().getNickname(), courseList.size(), scrapList.size());
    }

    private static class myInfo {
        private String profileUrl;
        private String nickname;
        private int courseCount;
        private int scrapCount;

        myInfo(final String profileUrl, final String nickname, final int courseCount, final int scrapCount) {
            this.profileUrl = profileUrl;
            this.nickname = nickname;
            this.courseCount = courseCount;
            this.scrapCount = scrapCount;
        }
    }
}