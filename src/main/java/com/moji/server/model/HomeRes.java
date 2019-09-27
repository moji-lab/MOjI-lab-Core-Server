package com.moji.server.model;

import lombok.Getter;
import lombok.Setter;

import com.moji.server.model.SearchRes.SearchCourseRes;

import java.util.List;

@Getter
@Setter
public class HomeRes {
    private String nickName;

    private String hotCategoryKeyword;

    private List<String> hotKeywords;
    private List<SearchCourseRes> hotCourseSearchCourseRes;

    private List<String> recommendKeywords;
    private List<SearchCourseRes> recommendCourseSearchCourseRes;

    private List<String> topKeywords;
}