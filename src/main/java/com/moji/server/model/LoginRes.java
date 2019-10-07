package com.moji.server.model;

import com.moji.server.domain.SignUp;
import lombok.Data;

/**
 * Created By ds on 29/09/2019.
 */

@Data
public class LoginRes {
    private String token;
    private int userIdx;
    private String profileUrl;
    private String nickname;
}