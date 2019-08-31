package com.moji.server.model;

import com.moji.server.domain.Hashtag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HashtagReq {
    private String courseIdx;
    private List<Hashtag> hashtags;
}
