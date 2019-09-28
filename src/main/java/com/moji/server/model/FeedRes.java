package com.moji.server.model;

import com.moji.server.domain.Photo;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class FeedRes {
    private String nickName;
    private String profileUrl;
    private LocalDate date;
    private String boardIdx;
    private List<Photo> photoList;
    private String place;
    private boolean isLiked;
    private int likeCount;
    private int commentCount;
    private String mainAddress;
    private boolean isScraped;
}