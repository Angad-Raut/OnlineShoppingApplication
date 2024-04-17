package com.projectx.microservice.authenticationservice.services;

import com.projectx.microservice.authenticationservice.entity.Users;
import com.projectx.microservice.authenticationservice.payloads.*;
import com.projectx.microservice.exceptions.AlreadyExistException;
import com.projectx.microservice.exceptions.ResourceNotFoundException;
import com.projectx.microservice.payloads.EntityIdDto;

import java.text.ParseException;
import java.util.List;

public interface UserService {
    Users getUserByUserName(String username);
    Boolean addUser(UserDto dto,Boolean isAdmin)throws AlreadyExistException, ResourceNotFoundException;
    UserDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    List<ViewUserDto> getAllUsers();
    Boolean isUserExist(Long mobile);
    UserDto buildUser();
    LoginResponse saveRefreshToken(String username) throws ParseException;
    AuthResponse getRefreshToken(String username) throws ParseException;
    CustomerInfoDto getUserInfoByMobile(EntityIdDto dto);

}
