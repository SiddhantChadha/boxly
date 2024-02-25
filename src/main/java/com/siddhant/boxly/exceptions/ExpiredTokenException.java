package com.siddhant.boxly.exceptions;

public class ExpiredTokenException extends RuntimeException{

    private String token;

    public ExpiredTokenException(String token){
        super(String.format("Token: %s is expired",token));
        this.token = token;
    }
}
