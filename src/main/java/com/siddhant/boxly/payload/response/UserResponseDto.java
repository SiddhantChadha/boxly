package com.siddhant.boxly.payload.response;

import lombok.Data;

@Data
public class UserResponseDto {

    private Integer id;
    private String email;
    private boolean isEnabled;

}
