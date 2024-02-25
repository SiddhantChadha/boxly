package com.siddhant.boxly.controllers;

import com.siddhant.boxly.payload.response.ApiResponse;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> findUser(@RequestParam String email){
        UserResponseDto userResponseDto = userService.findUserByEmailId(email);
        return new ResponseEntity<>(new ApiResponse<>(true,userResponseDto), HttpStatus.OK);
    }



}
