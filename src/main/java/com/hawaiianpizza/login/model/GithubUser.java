package com.hawaiianpizza.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUser {
    private String GithubId;
    private String email;
    private String name;
    private String access_token;
    private String id_token;

}
