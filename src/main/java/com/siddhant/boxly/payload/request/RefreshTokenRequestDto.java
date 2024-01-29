package com.siddhant.boxly.payload.request;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {

    private String refreshToken;
    private String email;

}
