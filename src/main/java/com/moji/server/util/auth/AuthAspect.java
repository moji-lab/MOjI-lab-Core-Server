package com.moji.server.util.auth;

import com.moji.server.model.DefaultRes;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By ds on 2019-08-16.
 */

@Slf4j
@Component
@Aspect
public class AuthAspect {

    private final static String AUTHORIZATION = "Authorization";

    /**
     * 실패 시 기본 반환 Response
     */
    private final static DefaultRes DEFAULT_RES = DefaultRes.res(401, "인증 실패");
    private final static ResponseEntity<DefaultRes> RES_RESPONSE_ENTITY = new ResponseEntity<>(DEFAULT_RES, HttpStatus.UNAUTHORIZED);

    private final HttpServletRequest httpServletRequest;

//    private final UserMapper userMapper;
//
//    private final JwtService jwtService;

//    public AuthAspect(final HttpServletRequest httpServletRequest, final JwtService jwtService, final UserMapper userMapper) {
//        this.httpServletRequest = httpServletRequest;
//        this.jwtService = jwtService;
//        this.userMapper = userMapper;
//    }

    public AuthAspect(final HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    //항상 @annotation 패키지 이름을 실제 사용할 annotation 경로로 맞춰줘야 한다.
    @Around("@annotation(com.moji.server.util.auth.Auth)")
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        final String jwt = httpServletRequest.getHeader(AUTHORIZATION);

        return pjp.proceed(pjp.getArgs());
    }
}