package com.siddhant.boxly.services.impl;

import com.siddhant.boxly.entities.RefreshToken;
import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.exceptions.ExpiredRefreshTokenException;
import com.siddhant.boxly.exceptions.ResourceNotFoundException;
import com.siddhant.boxly.payload.request.RefreshTokenRequestDto;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.repositories.RefreshTokenRepository;
import com.siddhant.boxly.repositories.UserRepository;
import com.siddhant.boxly.services.RefreshTokenService;
import com.siddhant.boxly.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,UserRepository userRepository,ModelMapper modelMapper){
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User","email",email));

        try {
            RefreshToken refreshToken =  refreshTokenRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Refresh Token","email",email));
            refreshTokenRepository.delete(refreshToken);
        }catch (ResourceNotFoundException resourceNotFoundException){

        }

        RefreshToken refreshToken = RefreshToken.builder().token(UUID.randomUUID().toString()).expiresAt(Instant.now().plusMillis(Constants.REFRESH_TOKEN_EXPIRY)).user(user).build();
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshToken);
        return savedRefreshToken.getToken();
    }

    @Override
    public UserResponseDto verifyRefreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequestDto.getRefreshToken()).orElseThrow(()->new ResourceNotFoundException("Refresh Token","id",refreshTokenRequestDto.getRefreshToken()));

        if(refreshToken.getExpiresAt().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            throw new ExpiredRefreshTokenException(refreshToken.getToken());
        }

        if(!refreshToken.getUser().getEmail().equals(refreshTokenRequestDto.getEmail())){
            throw new ResourceNotFoundException("Refresh Token","email",refreshTokenRequestDto.getEmail());
        }

        return modelMapper.map(refreshToken.getUser(), UserResponseDto.class);
    }
}
