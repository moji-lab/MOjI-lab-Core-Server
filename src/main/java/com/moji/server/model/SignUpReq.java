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
    private String photoUrl = "https://project-moji2.s3.ap-northeast-2.amazonaws.com/%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB.jpeg";
}