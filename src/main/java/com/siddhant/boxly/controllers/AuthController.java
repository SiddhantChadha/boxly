package com.siddhant.boxly.controllers;

import com.siddhant.boxly.payload.request.LoginRequestDto;
import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.ApiResponse;
import com.siddhant.boxly.payload.response.AuthenticatedResponse;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.services.AuthService;
import com.siddhant.boxly.services.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
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
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto){
        UserResponseDto userResponseDto = authService.signUp(signupRequestDto);
        return new ResponseEntity<>(new ApiResponse(true,userResponseDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto){
        AuthenticatedResponse authenticatedResponse = authService.login(loginRequestDto);
        return new ResponseEntity<>(new ApiResponse(true,authenticatedResponse),HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
         AuthenticatedResponse authenticatedResponse =  authService.verifyAndGenerateToken(refreshTokenRequestDto);
         return new ResponseEntity<>(new ApiResponse(true,authenticatedResponse),HttpStatus.OK);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<ApiResponse> confirmAccount(@RequestParam(name = "token") String confirmationToken){
           AuthenticatedResponse authenticatedResponse = authService.confirmAccount(confirmationToken);
           return new ResponseEntity<>(new ApiResponse(true,authenticatedResponse),HttpStatus.OK);
    }

}
