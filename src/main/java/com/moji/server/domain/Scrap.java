package com.moji.server.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created By ds on 25/09/2019.
 */

@Data
@Entity
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scrapIdx;
    private int boardIdx;
    private int userIdx;
}