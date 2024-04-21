package com.siddhant.boxly.exceptions;

import com.siddhant.boxly.payload.response.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ApiResponse> expiredRefreshTokenExceptionHandler(ExpiredTokenException expiredTokenException){
        return new ResponseEntity<>(new ApiResponse(false,expiredTokenException.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse> fileUploadExceptionHandler(FileUploadException fileUploadException){
        return new ResponseEntity<>(new ApiResponse(false,fileUploadException.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((err)->{
            sb.append(err.getDefaultMessage());
            sb.append("\n");
        });

        return new ResponseEntity<ApiResponse>(new ApiResponse(false,sb),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> sqlIntegrityConstraintViolationExceptionHandler(DataIntegrityViolationException e){
        return new ResponseEntity<>(new ApiResponse(false,"Username already exists"),HttpStatus.BAD_REQUEST);
    }

}
