package com.moji.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String mainAddress;
    private String subAddress;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime visitTime;
  
    private String content;
    private int order;
    private List<String> tagInfo = new ArrayList<String>();
    private String lat;
    private String lng;

    @Field
    private String boardIdx;

    @Field
    private int userIdx=1;

    //사진
    private List<Photo> photos = new ArrayList<Photo>();

    //댓글
    private List<Comment> comments = new ArrayList<Comment>();

    //해시태그
}
