package com.moji.server.domain;

import lombok.Data;

public class SearchResult {
    @Data
    static public class CourseSearchResult{
        private Course course;
        private User writer;
        private int likeCount;

        // 댓글 기능 구현 후 추가
        private int commentCount;
    }

    @Data
    static public class BoardSearchResult{
        private Board board;
        private User writer;
        private int likeCount;

        // 댓글 기능 구현 후 추가
        private int commentCount;
    }
}
