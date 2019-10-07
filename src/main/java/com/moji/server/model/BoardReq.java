package com.moji.server.model;

import com.moji.server.domain.Board;
import com.moji.server.domain.Course;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.CloneUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardReq implements Cloneable {

    //보드
    private Board info;
    //코스
    private List<Course> courses = new ArrayList<Course>();


    @Override
    public Object clone() throws CloneNotSupportedException {
        BoardReq boardReq = (BoardReq) super.clone();
        boardReq.info = (Board) CloneUtils.clone(boardReq.info);

        List<Course> tmpt = new ArrayList<>();

        for (int i = 0; i < boardReq.courses.size(); i++) {
            tmpt.add((Course) CloneUtils.clone(boardReq.courses.get(i)));
        }

        boardReq.courses = tmpt;
        return boardReq;
    }
}
