package com.siddhant.boxly.services.impl;

import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.entities.VerificationToken;
import com.siddhant.boxly.exceptions.ExpiredTokenException;
import com.siddhant.boxly.exceptions.ResourceNotFoundException;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.repositories.UserRepository;
import com.siddhant.boxly.repositories.VerificationTokenRepository;
import com.siddhant.boxly.services.VerificationTokenService;
import com.siddhant.boxly.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository, UserRepository userRepository, ModelMapper modelMapper){
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public UserResponseDto checkToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(()->new ResourceNotFoundException("Verification Token","value",token));

        if(verificationToken.getExpiresAt().compareTo(Instant.now()) < 0){
            throw new ExpiredTokenException(verificationToken.getToken());
        }

        User user =  verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public String createToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User","email",email));
        VerificationToken verificationToken = VerificationToken.builder()
                .token(UUID.randomUUID().toString()).user(user)
                .expiresAt(Instant.now().plusMillis(Constants.CONFIRMATION_TOKEN_EXPIRY)).build();
        VerificationToken savedVerificationToken = verificationTokenRepository.save(verificationToken);
        return savedVerificationToken.getToken();
    }
}
