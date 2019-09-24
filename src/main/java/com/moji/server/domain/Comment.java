package com.moji.server.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private int userIdx;
    private String content;
    private Date writeTime;
}
