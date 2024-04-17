package com.projectx.microservice.authenticationservice.controller;

import com.projectx.microservice.authenticationservice.entity.TokenDetails;
import com.projectx.microservice.authenticationservice.payloads.*;
import com.projectx.microservice.authenticationservice.services.JwtService;
import com.projectx.microservice.authenticationservice.services.TokenService;
import com.projectx.microservice.authenticationservice.services.UserService;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.InvalidUserException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.exceptions.TokenExpiryedException;
import com.projectx.microservice.payloads.EntityIdDto;
import com.projectx.microservice.payloads.ResponseDto;
import com.projectx.microservice.utils.Constants;
import com.projectx.microservice.utils.ErrorHandlerComponent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> authenticate(@Valid @RequestBody AuthRequest requestDto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                LoginResponse response = userService.saveRefreshToken(requestDto.getUsername());
                return new ResponseEntity<ResponseDto<LoginResponse>>(new ResponseDto<LoginResponse>(response,null), HttpStatus.OK);
            } else {
                throw new InvalidUserException(Constants.INVALID_USER);
            }
        } catch (TokenExpiryedException | InvalidUserException e) {
            return errorHandler.handleError(e);
        }  catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDto<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest requestDto,BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            AuthResponse response = userService.getRefreshToken(requestDto.getUserName());
            return new ResponseEntity<ResponseDto<AuthResponse>>(new ResponseDto<AuthResponse>(response,null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public ResponseEntity<ResponseDto<Boolean>> logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean result = false;
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            result = true;
        }
        return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(result,""), HttpStatus.OK);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<ResponseDto<Boolean>> registerUser(@Valid @RequestBody UserDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = userService.addUser(dto,false);
            return new ResponseEntity<ResponseDto<Boolean>>(new ResponseDto<Boolean>(data,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getUserDetailsById")
    public ResponseEntity<ResponseDto<UserDto>> getUserDetailsById(@Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            UserDto data = userService.getById(dto);
            return new ResponseEntity<ResponseDto<UserDto>>(new ResponseDto<UserDto>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ResponseDto<List<ViewUserDto>>> getAllUsers() {
        try {
            List<ViewUserDto> data = userService.getAllUsers();
            return new ResponseEntity<ResponseDto<List<ViewUserDto>>>(new ResponseDto<List<ViewUserDto>>(data,null), HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping("/getUserInfoByMobile")
    public ResponseEntity<ResponseDto<CustomerInfoDto>> getUserInfoByMobile(@Valid @RequestBody EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()) {
            return errorHandler.handleValidationErrors(result);
        }
        try {
            CustomerInfoDto data = userService.getUserInfoByMobile(dto);
            return new ResponseEntity<ResponseDto<CustomerInfoDto>>(new ResponseDto<CustomerInfoDto>(data,null), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        }
    }
}
