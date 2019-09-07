package com.moji.server.model;

import com.moji.server.domain.Comment;
import com.moji.server.domain.Course;
import com.moji.server.domain.Photo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class CourseRes {

    Course course;

    // did like
    private boolean isLiked;

    // did scrap
    private boolean isScraped;

    // like count
    private int likeCount;

    // scrap count
    private int scrapCount;
}
