package com.moji.server.model;

import com.moji.server.domain.Scrap;
import lombok.Data;

/**
 * Created By ds on 25/09/2019.
 */

@Data
public class ScrapReq {
    private int scrapIdx;
    private int boardIdx;
    private int userIdx;

    public Scrap toScrap() {
        Scrap scrap = new Scrap();
        scrap.setBoardIdx(boardIdx);
        scrap.setScrapIdx(scrapIdx);
        scrap.setUserIdx(userIdx);
        return scrap;
    }
}