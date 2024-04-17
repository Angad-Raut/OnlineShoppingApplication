package com.projectx.microservice.authenticationservice.payloads;

import com.projectx.microservice.authenticationservice.validators.UserValid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UserValid
public class UserDto {
    private Long userId;
    private String userName;
    @Email
    private String userEmail;
    private Long userMobile;
    private String userPassword;
    private String gender;
    private AddressDto addressDto;
}
