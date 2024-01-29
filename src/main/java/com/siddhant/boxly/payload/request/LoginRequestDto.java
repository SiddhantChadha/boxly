package com.siddhant.boxly.payload.request;

import lombok.Getter;

@Getter
public class LoginRequestDto {

    private String email;
    private String password;
}
