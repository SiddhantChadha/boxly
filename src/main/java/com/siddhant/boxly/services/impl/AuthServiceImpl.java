package com.siddhant.boxly.services.impl;

import com.siddhant.boxly.payload.request.LoginRequestDto;
import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.AuthenticatedResponse;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.services.AuthService;
import com.siddhant.boxly.services.RefreshTokenService;
import com.siddhant.boxly.services.UserService;
import com.siddhant.boxly.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private ModelMapper modelMapper;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public AuthServiceImpl(UserService userService,PasswordEncoder passwordEncoder,JwtUtil jwtUtil,AuthenticationManager authenticationManager,ModelMapper modelMapper,RefreshTokenService refreshTokenService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Transactional
    public AuthenticatedResponse signUp(SignupRequestDto signupRequestDto) {
        String encodedPassword  = passwordEncoder.encode(signupRequestDto.getPassword());
        signupRequestDto.setPassword(encodedPassword);
        UserResponseDto userResponseDto = userService.createUser(signupRequestDto);
        String token = jwtUtil.generateToken(userResponseDto.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(userResponseDto.getEmail());
        return new AuthenticatedResponse(token,refreshToken,userResponseDto);
    }

    @Override
    public AuthenticatedResponse login(LoginRequestDto loginRequestDto) throws BadCredentialsException {
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),loginRequestDto.getPassword()));
        UserResponseDto userResponseDto = modelMapper.map(authentication.getPrincipal(),UserResponseDto.class);
        String token = jwtUtil.generateToken(userResponseDto.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(userResponseDto.getEmail());
        return new AuthenticatedResponse(token,refreshToken,userResponseDto);
    }

    public AuthenticatedResponse verifyAndGenerateToken(RefreshTokenRequestDto refreshTokenRequestDto){
        UserResponseDto userResponseDto =  refreshTokenService.verifyRefreshToken(refreshTokenRequestDto);
        String token = jwtUtil.generateToken(userResponseDto.getEmail());
        String refreshToken = refreshTokenService.createRefreshToken(userResponseDto.getEmail());
        return new AuthenticatedResponse(token,refreshToken,userResponseDto);
    }

}
