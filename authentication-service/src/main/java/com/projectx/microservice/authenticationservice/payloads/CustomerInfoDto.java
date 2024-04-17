package com.projectx.microservice.authenticationservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerInfoDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private Long userMobile;
    private String gender;
    private String street;
    private String city;
    private String state;
    private Integer pinCode;
}
