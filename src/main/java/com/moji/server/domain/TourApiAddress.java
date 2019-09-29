package com.moji.server.domain;

import lombok.Data;

@Data
public class TourApiAddress {
    private String mainAddress;
    private String subAddress;

    //위도
    private String lat;
    //경도
    private String lng;
}
