package com.moji.server.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class SearchResult {
    @Data
    static public class CourseSearchResult implements Comparable<CourseSearchResult> {
        Course course;

        // did like
        private boolean isLiked;

        // did scrap
        private boolean isScraped;

        // like count
        private int likeCount;

        // scrap count
        private int scrapCount;

        @Override
        public int compareTo(CourseSearchResult courseSearchResult) {
            if(likeCount > courseSearchResult.likeCount) return -1;
            else if(likeCount < courseSearchResult.likeCount) return 1;
            return 0;
        }
    }

    @Data
    static public class BoardSearchResult implements Comparable<BoardSearchResult>{
        private String nickName;
        private String profileUrl;
        private LocalDate date;
        private String boardIdx;
        private List<Photo> photoList;
        private String place;
        private boolean isLiked;
        private int likeCount;
        private int commentCount;

        @Override
        public int compareTo(BoardSearchResult boardSearchResult) {
            if(likeCount > boardSearchResult.likeCount) return -1;
            else if(likeCount < boardSearchResult.likeCount) return 1;
            return 0;
        }
    }
}
