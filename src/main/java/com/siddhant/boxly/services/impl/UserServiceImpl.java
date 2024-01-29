package com.siddhant.boxly.services.impl;

import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.exceptions.ResourceNotFoundException;
import com.siddhant.boxly.payload.request.SignupRequestDto;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.repositories.UserRepository;
import com.siddhant.boxly.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto createUser(SignupRequestDto signupRequestDto) {
        User user = modelMapper.map(signupRequestDto,User.class);
        User savedUser =  userRepository.save(user);
        return modelMapper.map(savedUser,UserResponseDto.class);
    }

    @Override
    public UserResponseDto findUserByEmailId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User","email",email));
        return modelMapper.map(user,UserResponseDto.class);
    }


}
