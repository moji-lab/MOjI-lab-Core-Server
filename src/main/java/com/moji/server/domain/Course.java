package com.moji.server.domain;

import lombok.Data;
import org.apache.http.client.utils.CloneUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "course")
public class Course implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String mainAddress;
    private String subAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitTime;
  
    private String content;
    private int order;
    private List<String> tagInfo = new ArrayList<String>();
    //위도
    private String lat;
    //경도
    private String lng;

    @Field
    private String boardIdx;

    @Field
    private int userIdx;

    //사진
    private List<Photo> photos = new ArrayList<Photo>();

    //댓글
    private List<Comment> comments = new ArrayList<Comment>();


    @Override
    public Object clone() throws CloneNotSupportedException{
        Course course = (Course)super.clone();
        course.tagInfo = (List<String>) CloneUtils.clone(course.tagInfo);

        List<Photo> tmpt = new ArrayList<>();

        for(int i = 0; i < course.photos.size(); i++)
        {
            tmpt.add((Photo)CloneUtils.clone(course.photos.get(i)));
        }

        course.photos = tmpt;
//        course.photos = (List<Photo>) CloneUtils.clone(course.photos);
        return course;
    }
}
