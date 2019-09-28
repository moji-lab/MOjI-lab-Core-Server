package com.moji.server.model;

import com.moji.server.domain.Comment;
import com.moji.server.domain.User;
import lombok.Data;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class BoardRes {
    // user info
    private User user;
    private String _id;
    // user writeTime
    private LocalDate writeTime;

    private int likeCount;
    List<CourseRes> courseList;
    private boolean isScraped;
    private boolean isLiked;
    private List<Comment> comments = new ArrayList<Comment>();
}
