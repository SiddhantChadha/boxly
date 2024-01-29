package com.siddhant.boxly.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException{

    private String refreshToken;

    public ExpiredRefreshTokenException(String refreshToken){
        super(String.format("Refresh Token: %s not found",refreshToken));
        this.refreshToken = refreshToken;
    }
}
