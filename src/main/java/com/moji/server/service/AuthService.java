package com.moji.server.service;

import com.moji.server.domain.SignUp;
import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.LoginReq;
import com.moji.server.repository.SignUpRepository;
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
public class AuthService {

    private final SignUpRepository signUpRepository;
    private final JwtService jwtService;

    public AuthService(final JwtService jwtService, final SignUpRepository signUpRepository) {
        this.jwtService = jwtService;
        this.signUpRepository = signUpRepository;
    }

    public DefaultRes login(final LoginReq loginReq) {
        try {
            AES256Util aes256Util = new AES256Util("MOJI-SERVER-ENCRYPT-TEST");
            Optional<SignUp> user = signUpRepository.findByEmailAndPassword(loginReq.getEmail(),
                    aes256Util.encrypt(loginReq.getPassword()));
            if (user.isPresent()) {
                final String token = jwtService.create(user.get().getUserIdx());
                return DefaultRes.res(StatusCode.CREATED, "로그인 성공", token);
            }
            return DefaultRes.res(StatusCode.UNAUTHORIZED, "로그인 실패");
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.UNAUTHORIZED, "로그인 실패");
        }
    }

    public int authorization(final String jwt) {
        final int userIdx = jwtService.decode(jwt).getUser_idx();
        if (userIdx == -1) return -1;

        final Optional<SignUp> user = signUpRepository.findById(userIdx);
        if (!user.isPresent()) return -1;

        return userIdx;
    }
}