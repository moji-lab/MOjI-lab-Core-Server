package com.moji.server.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By ds on 26/09/2019.
 */

@Data
@NoArgsConstructor
@Builder
public class MyPageRes<T> {
    private String nickname;
    private String profileUrl;
    private int courseCount;
    private int scrapCount;
    private List<T> feedList;
}