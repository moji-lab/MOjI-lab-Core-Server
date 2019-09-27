package com.moji.server.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data
public class PersonRes {
    private String email;
    private String nickname;
    private int userIdx;
    private String photoUrl;
}
