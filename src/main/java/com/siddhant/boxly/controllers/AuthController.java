package com.siddhant.boxly.controllers;

import com.siddhant.boxly.payload.request.LoginRequestDto;
import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.ApiResponse;
import com.siddhant.boxly.payload.response.AuthenticatedResponse;
import com.siddhant.boxly.services.AuthService;
import com.siddhant.boxly.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthService authService,RefreshTokenService refreshTokenService){
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignupRequestDto signupRequestDto){
        AuthenticatedResponse authenticatedResponse = authService.signUp(signupRequestDto);
        return new ResponseEntity<>(new ApiResponse(true,authenticatedResponse), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto){
        AuthenticatedResponse authenticatedResponse = authService.login(loginRequestDto);
        return new ResponseEntity<>(new ApiResponse(true,authenticatedResponse),HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
         AuthenticatedResponse authenticatedResponse =  authService.verifyAndGenerateToken(refreshTokenRequestDto);
         return new ResponseEntity<>(new ApiResponse(true,authenticatedResponse),HttpStatus.OK);
    }


}
