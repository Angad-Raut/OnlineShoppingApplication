package com.projectx.microservice.exceptions;

public class TokenExpiryedException extends RuntimeException{
    public TokenExpiryedException(String msg) {
        super(msg);
    }
}
