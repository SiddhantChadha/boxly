package com.siddhant.boxly.exceptions;

import com.siddhant.boxly.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundException){
        return new ResponseEntity<>(new ApiResponse(false,resourceNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse> usernameNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException){
        return new ResponseEntity<>(new ApiResponse(false,usernameNotFoundException.getMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> badCredentialsExceptionHandler(BadCredentialsException badCredentialsException){
        return new ResponseEntity<>(new ApiResponse(false,badCredentialsException.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ApiResponse> expiredRefreshTokenExceptionHandler(ExpiredRefreshTokenException expiredRefreshTokenException){
        return new ResponseEntity<>(new ApiResponse(false,expiredRefreshTokenException.getMessage()),HttpStatus.BAD_REQUEST);
    }

}
