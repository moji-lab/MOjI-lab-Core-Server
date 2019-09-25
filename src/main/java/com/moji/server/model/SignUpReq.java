package com.moji.server.model;

import com.moji.server.domain.User;
import lombok.Data;

/**
 * Created By ds on 25/09/2019.
 */

@Data
public class SignUpReq {
    private String password;
    private String nickname;
    private String email;
    private String photoUrl;

    public User toUser() {
        return User.builder().nickname(password).photoUrl(photoUrl).email(email).password(password).build();
    }
}