package com.moji.server.model;

import com.moji.server.domain.Board;
import com.moji.server.domain.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardReq {

    //보드
    private Board info;
    //코스
    private List<Course> courses = new ArrayList<Course>();
}
