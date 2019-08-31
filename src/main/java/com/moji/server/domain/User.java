package com.moji.server.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created By ds on 2019-08-20.
 */

@Data
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    private String password;
    private String nickname;
    private String email;
    private String photoUrl;
}
