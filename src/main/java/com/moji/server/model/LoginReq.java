package com.moji.server.model;

import lombok.Data;

/**
 * Created By ds on 2019-08-20.
 */

@Data
public class LoginReq {
    private String email;
    private String password;
}