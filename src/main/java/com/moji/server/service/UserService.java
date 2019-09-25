package com.moji.server.service;

import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SignUpReq;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.AES256Util;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created By ds on 2019-08-20.
 */

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final S3FileUploadService s3FileUploadService;

    public UserService(final UserRepository userRepository, final S3FileUploadService s3FileUploadService) {
        this.userRepository = userRepository;
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 마이 페이지 조회
     * 내가 스크랩 한 글 갯수
     * 내 피드들 조회
     * 마이페이지- 프로필사진, 닉네임 데이터 줄때 나의 기록 총 게시글 개수도 같이 보내줘
     *
     * @param userIdx
     * @return
     */
    public DefaultRes<User> findByUserIdx(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);
        return user.map(value -> DefaultRes.res(StatusCode.OK, "사용자 정보 조회 완료", value))
                .orElseGet(() -> DefaultRes.res(StatusCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    /**
     * 회원 정보 저장
     * todo : 롤백 처리, 이미지 업로드
     *
     * @return
     */
    public DefaultRes saveUser(final SignUpReq signUpReq) {
        try {
            AES256Util aes256Util = new AES256Util("MOJI-SERVER-ENCRYPT-TEST");
            signUpReq.setPassword(aes256Util.encrypt(signUpReq.getPassword()));
            userRepository.save(signUpReq.toUser());
            return DefaultRes.res(StatusCode.CREATED, "회원 가입 완료");
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, "데이터베이스 에러");
        }
    }

    /**
     * 이메일 중복 확인
     *
     * @param email
     * @return
     */
    public DefaultRes validateEmail(final String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(value -> DefaultRes.res(StatusCode.BAD_REQUEST, "중복된 이메일입니다.", value)).orElseGet(() -> DefaultRes.res(StatusCode.OK, "사용 가능 합니다."));
    }

    /**
     * 이메일 중복 확인
     *
     * @param nickName
     * @return
     */
    public DefaultRes validateNickName(final String nickName) {
        Optional<User> user = userRepository.findByNickname(nickName);
        return user.map(value -> DefaultRes.res(StatusCode.BAD_REQUEST, "중복된 닉네임입니다.(", value)).orElseGet(() -> DefaultRes.res(StatusCode.OK, "사용 가능 합니다."));
    }
}
