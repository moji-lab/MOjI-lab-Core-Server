package com.moji.server.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created By ds on 25/09/2019.
 */

@Data
@Entity
@Table(name = "SCRAP")
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scrapIdx;

    private String boardIdx;
    private int userIdx;
}