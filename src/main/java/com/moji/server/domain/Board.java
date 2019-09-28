package com.moji.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.CloneUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "board")
public class Board implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String _id;

    private String mainAddress;
    private String subAddress;

    private Date writeTime;
    private boolean open = true;

    private int userIdx;

    //공유 친구
    private List<Integer> share = new ArrayList<Integer>();

    // 댓글
    private List<Comment> comments = new ArrayList<Comment>();


    @Override
    public Object clone() throws CloneNotSupportedException{
        Board board = (Board) super.clone();
        board.share = (List<Integer>) CloneUtils.clone(board.share);
        return board;
    }

}
