package com.siddhant.boxly.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticatedResponse<T>{

    private String accessToken;
    private String refreshToken;
    private UserResponseDto userDetails;
}
