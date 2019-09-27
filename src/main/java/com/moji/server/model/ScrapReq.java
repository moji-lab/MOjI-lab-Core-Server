package com.moji.server.model;

import com.moji.server.domain.Scrap;
import lombok.Data;

/**
 * Created By ds on 25/09/2019.
 */

@Data
public class ScrapReq {
    private String boardIdx;
    private int userIdx;

    public Scrap toScrap() {
        Scrap scrap = new Scrap();
        scrap.setBoardIdx(boardIdx);
        scrap.setUserIdx(userIdx);
        return scrap;
    }
}