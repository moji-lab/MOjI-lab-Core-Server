package com.moji.server.model;

import com.moji.server.domain.Board;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardRes2 {

    private BoardRes2 (){

    }

    private static BoardRes2 boardRes2;
    public static BoardRes2 getBoardRes2(){
        if(boardRes2 == null){
            boardRes2 = new BoardRes2();
        }
        return boardRes2;
    }

    List<String> courseIdx = new ArrayList<>();
}
