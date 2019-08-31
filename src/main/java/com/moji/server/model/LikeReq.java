package com.moji.server.model;

import lombok.Data;

@Data
public class LikeReq {
    // 좋아요한 유저 idx
    private int userIdx;
    // board일 때는 boardIdx, course일 때는 courseIdx가 전달됨
    private String postIdx;
}
