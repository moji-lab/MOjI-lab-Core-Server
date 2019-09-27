package com.moji.server.model;

import com.moji.server.domain.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BoardRes {
    // user info
    private User user;
    private String _id;
    // user writeTime
    private Date writeTime;

    private int likeCount;
    List<CourseRes> courseList;
    private boolean isScraped;
    private boolean isLiked;
}
