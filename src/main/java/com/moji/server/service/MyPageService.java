package com.moji.server.service;

import com.moji.server.domain.Board;
import com.moji.server.domain.Scrap;
import com.moji.server.domain.User;
import com.moji.server.model.BoardRes;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.MyPageRes;
import com.moji.server.repository.BoardRepository;
import com.moji.server.repository.ScrapRepository;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final BoardRepository boardRepository;
    private final ScrapRepository scrapRepository;
    private final BoardService boardService;

    public MyPageService(final UserRepository userRepository,
                         final BoardRepository boardRepository,
                         final ScrapRepository scrapRepository,
                         final BoardService boardService) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.scrapRepository = scrapRepository;
        this.boardService = boardService;
    }

    /**
     * 마이페이지- 프로필사진, 닉네임 데이터 줄때 나의 기록 총 게시글 개수도 같이 보내줘
     * <p>
     * 마이 페이지 1 - 나의 기록하기 리스트(친구와 공유한 기록하기 리스트 포함)
     * 마이 페이지 2 - 스크랩 한 글 리스트
     */

    public DefaultRes<MyPageRes> getMyBoardList(final int userIdx) {
        DefaultInfo defaultInfo = getMyPageDefaultInfo(userIdx);
        MyPageRes myPageRes = getMyPageRes(defaultInfo);
        myPageRes.setFeedList((List<BoardRes>) boardService.getBoardList(userIdx).getData());
        return DefaultRes.res(StatusCode.OK, "조회 성공", myPageRes);
    }

    public DefaultRes<MyPageRes> getMyScrapCourseList(final int userIdx) {
        DefaultInfo defaultInfo = getMyPageDefaultInfo(userIdx);
        MyPageRes myPageRes = getMyPageRes(defaultInfo);
        List<Board> list = new LinkedList<>();
        for (Scrap scrap : defaultInfo.scrapList) {
            list.add(boardRepository.findBy_id(scrap.getBoardIdx()).get());
        }
        myPageRes.setFeedList((List<BoardRes>) boardService.getScrapList(userIdx, list).getData());
        return DefaultRes.res(StatusCode.OK, "조회 성공", myPageRes);
    }

    private MyPageRes getMyPageRes(final DefaultInfo defaultInfo) {
        MyPageRes myPageRes = new MyPageRes();
        myPageRes.setNickname(defaultInfo.nickname);
        myPageRes.setProfileUrl(defaultInfo.profileUrl);
        myPageRes.setBoardCount(defaultInfo.boardList.size());
        myPageRes.setScrapCount(defaultInfo.scrapList.size());
        return myPageRes;
    }

    //기본으로 프로필 사진, 닉네임, 나의 기록(친구와 공유), 스크랩 한 글 갯수 포함
    private DefaultInfo getMyPageDefaultInfo(final int userIdx) {
        Optional<User> user = userRepository.findByUserIdx(userIdx);
        List<Scrap> scrapList = scrapRepository.findByUserIdx(userIdx);
        List<Board> boardList = boardRepository.findByUserIdx(userIdx);
        return new DefaultInfo(user.get().getPhotoUrl(), user.get().getNickname(), boardList, scrapList);
    }

    private static class DefaultInfo {
        private String profileUrl;
        private String nickname;
        private List<Board> boardList;
        private List<Scrap> scrapList;

        DefaultInfo(final String profileUrl, final String nickname, final List<Board> courseList, final List<Scrap> scrapList) {
            this.profileUrl = profileUrl;
            this.nickname = nickname;
            this.boardList = courseList;
            this.scrapList = scrapList;
        }
    }
}