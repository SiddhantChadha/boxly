package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.response.UserResponseDto;

public interface VerificationTokenService {

    UserResponseDto checkToken(String token);
    String createToken(String email);

}
