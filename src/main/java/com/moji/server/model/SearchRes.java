package com.moji.server.model;

import com.moji.server.domain.SearchResult.BoardSearchResult;
import com.moji.server.domain.SearchResult.CourseSearchResult;
import lombok.*;

import java.util.List;

@Data
public class SearchRes{
    @Data
    @AllArgsConstructor
    static public class SearchBoardRes{
        private final int type = 0;
        private List<BoardSearchResult> boards;
    }
    @Data
    @AllArgsConstructor
    static public class SearchCourseRes{
        private final int type = 1;
        private List<CourseSearchResult> courses;
    }
}
