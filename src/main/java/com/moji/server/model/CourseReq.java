package com.moji.server.model;

import com.moji.server.domain.Photo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By ds on 07/10/2019.
 */

@Data
public class CourseReq {
    private String boardIdx;
    private String courseIdx;
    private String mainAddress;
    private String subAddress;
    private int userIdx;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitTime;

    private String content;
    private int order;
    private List<String> tagInfo = new ArrayList<String>();

    //위도
    private String lat;
    //경도
    private String lng;

    private List<Photo> photos = new ArrayList<Photo>();
}