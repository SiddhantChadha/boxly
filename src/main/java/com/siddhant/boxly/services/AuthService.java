package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.request.LoginRequestDto;
import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.AuthenticatedResponse;

public interface AuthService {

        AuthenticatedResponse signUp(SignupRequestDto signupRequestDto);
        AuthenticatedResponse login (LoginRequestDto loginRequestDto);

        AuthenticatedResponse verifyAndGenerateToken(RefreshTokenRequestDto refreshTokenRequestDto);

}
