package com.moji.server.domain;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "ADDRESS")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressIdx;

    private String mainAddress;
    private String subAddress;

    private String lat;
    private String lng;
}
