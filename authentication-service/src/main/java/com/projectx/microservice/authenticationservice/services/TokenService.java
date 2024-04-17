package com.projectx.microservice.authenticationservice.services;

import com.projectx.microservice.authenticationservice.entity.TokenDetails;
import com.projectx.microservice.authenticationservice.payloads.AuthResponse;
import com.projectx.microservice.authenticationservice.payloads.LoginResponse;

import java.text.ParseException;

public interface TokenService {
    LoginResponse getTokenDetails(String username) throws ParseException;
    TokenDetails saveRefreshToken(String username) throws ParseException;
    TokenDetails validateRefreshToken(TokenDetails token, String username) throws ParseException;
    Boolean isRefreshTokenExpiry(TokenDetails tokenDetails) throws ParseException;
    Boolean isRefreshTokenExist(String token);
    AuthResponse getRefreshToken(String username) throws ParseException;
}
