package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.request.LoginRequestDto;
import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.AuthenticatedResponse;
import com.siddhant.boxly.payload.response.UserResponseDto;

public interface AuthService {

        UserResponseDto signUp(SignupRequestDto signupRequestDto);
        AuthenticatedResponse login (LoginRequestDto loginRequestDto);

        AuthenticatedResponse confirmAccount(String confirmationToken);

        AuthenticatedResponse verifyAndGenerateToken(RefreshTokenRequestDto refreshTokenRequestDto);

}
