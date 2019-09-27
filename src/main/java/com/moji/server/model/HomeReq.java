package com.moji.server.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeReq {
    List<String> keywords;
}
