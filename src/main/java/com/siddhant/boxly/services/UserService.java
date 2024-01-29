package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(SignupRequestDto signupRequestDto);
    UserResponseDto findUserByEmailId(String email);

}
