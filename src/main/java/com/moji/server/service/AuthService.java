package com.moji.server.service;

import com.moji.server.domain.User;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.LoginReq;
import com.moji.server.repository.UserRepository;
import com.moji.server.util.StatusCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created By ds on 2019-08-20.
 */

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(final UserRepository userRepository, final JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public DefaultRes login(final LoginReq loginReq) {
        Optional<User> user = userRepository.findByEmailAndPassword(loginReq.getEmail(), loginReq.getPassword());
        if(user.isPresent()) {
            final JwtService.TokenRes tokenRes = jwtService.create(user.get().getUserIdx());
            return DefaultRes.res(StatusCode.CREATED, "로그인 성공", tokenRes);
        }
        return DefaultRes.res(StatusCode.UNAUTHORIZED, "로그인 실패");
    }
}