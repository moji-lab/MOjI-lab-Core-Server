package com.moji.server.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created By ds on 25/09/2019.
 */

@Data
public class SignUpReq {
    private String password;
    private String nickname;
    private String email;
    private String photoUrl;
}