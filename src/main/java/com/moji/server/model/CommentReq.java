package com.moji.server.model;

import lombok.Data;

@Data
public class CommentReq {
    // 댓글 유저 idx
    private int userIdx;
    // 댓글 내용
    private String content;
    // board일 때는 boardIdx, course일 때는 courseIdx가 전달됨
    private String postIdx;
}
