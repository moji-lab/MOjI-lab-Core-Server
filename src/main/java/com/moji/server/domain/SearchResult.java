package com.moji.server.domain;

import lombok.Data;

public class SearchResult {
    @Data
    static public class CourseSearchResult implements Comparable<CourseSearchResult> {
        private Course course;
        private User writer;
        private int likeCount;

        // 댓글 기능 구현 후 추가
        private int commentCount;

        @Override
        public int compareTo(CourseSearchResult courseSearchResult) {
            if(likeCount > courseSearchResult.likeCount) return -1;
            else if(likeCount < courseSearchResult.likeCount) return 1;
            return 0;
        }
    }

    @Data
    static public class BoardSearchResult implements Comparable<BoardSearchResult>{
        private Board board;
        private User writer;
        private int likeCount;

        // 댓글 기능 구현 후 추가
        private int commentCount;

        @Override
        public int compareTo(BoardSearchResult boardSearchResult) {
            if(likeCount > boardSearchResult.likeCount) return -1;
            else if(likeCount < boardSearchResult.likeCount) return 1;
            return 0;
        }
    }
}
