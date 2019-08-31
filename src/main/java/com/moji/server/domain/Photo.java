package com.moji.server.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Data
public class Photo {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private String _id;

    private MultipartFile photo;
    private String photoUrl;
    private boolean represent;
//    private String courseIdx;

}
