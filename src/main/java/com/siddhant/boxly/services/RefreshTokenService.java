package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.response.UserResponseDto;

public interface RefreshTokenService {

    String createRefreshToken(String email);
    UserResponseDto verifyRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto);

}
