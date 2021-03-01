package com.hawaiianpizza.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "user")
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String email;

    private String logintype;

    private String token;
    private String name;

    private String profileimage;

    private String pwd;

    private String certified;
    private String certifycode;
}
