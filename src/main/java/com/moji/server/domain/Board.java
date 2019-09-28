package com.moji.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String mainAddress;
    private String subAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate writeTime;
    private boolean open;

    private int userIdx;

    //공유 친구
    private List<Integer> share = new ArrayList<Integer>();

    // 댓글
    private List<Comment> comments = new ArrayList<Comment>();
}
