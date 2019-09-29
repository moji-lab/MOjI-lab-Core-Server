package com.moji.server.service;

import com.moji.server.domain.SignUp;
import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.SignUpReq;
import com.moji.server.repository.SignUpRepository;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.AES256Util;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.moji.server.model.DefaultRes.BAD_REQUEST;
import static com.moji.server.model.DefaultRes.DB_ERROR;

/**
 * Created By ds on 2019-08-20.
 */

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SignUpRepository signUpRepository;

    private final S3FileUploadService s3FileUploadService;

    public UserService(final UserRepository userRepository,
                       final S3FileUploadService s3FileUploadService,
                       final SignUpRepository signUpRepository) {
        this.userRepository = userRepository;
        this.s3FileUploadService = s3FileUploadService;
        this.signUpRepository = signUpRepository;
    }

    /**
     * 마이 페이지 조회
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
     * 회원 프로필 사진 조회
     *
     * @param userIdx
     * @return
     */
    public Optional<String> findPhotoUrlByUserIdx(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);
        return user.map(value -> value.getPhotoUrl());
    }

    /**
     * 회원 정보 저장
     * @param signUpReq
     * @return
     */
    @Transactional
    public DefaultRes saveUser(final SignUpReq signUpReq) {
        try {
            AES256Util aes256Util = new AES256Util("MOJI-SERVER-ENCRYPT-TEST");
            signUpReq.setPassword(aes256Util.encrypt(signUpReq.getPassword()));
            SignUp signUp = SignUp.builder()
                    .email(signUpReq.getEmail())
                    .password(signUpReq.getPassword())
                    .nickname(signUpReq.getNickname())
                    .photoUrl(signUpReq.getPhotoUrl())
                    .build();
            signUpRepository.save(signUp);
            return DefaultRes.res(StatusCode.CREATED, "회원 가입 완료");
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DB_ERROR;
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
        return user.map(value -> DefaultRes.res(StatusCode.BAD_REQUEST, "중복된 이메일입니다.")).orElseGet(() -> DefaultRes.res(StatusCode.OK, "사용 가능 합니다."));
    }

    /**
     * 이메일 중복 확인
     *
     * @param nickName
     * @return
     */
    public DefaultRes validateNickName(final String nickName) {
        Optional<User> user = userRepository.findByNickname(nickName);
        return user.map(value -> DefaultRes.res(StatusCode.BAD_REQUEST, "중복된 닉네임입니다.")).orElseGet(() -> DefaultRes.res(StatusCode.OK, "사용 가능 합니다."));
    }

    @Transactional
    public DefaultRes changeProfileImage(final int userIdx, final MultipartFile multipartFile) {
        try {
            if(multipartFile.isEmpty()) return BAD_REQUEST;
            final String url = s3FileUploadService.upload(multipartFile);
            User user = userRepository.findById(userIdx).get();
            user.setPhotoUrl(url);
            userRepository.save(user);
            return DefaultRes.res(StatusCode.CREATED, "사진 수정 완료");
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DB_ERROR;
        }
    }
}
