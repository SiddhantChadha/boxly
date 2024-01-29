package com.siddhant.boxly.payload.request;

import lombok.Data;

@Data
public class SignupRequestDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

}
