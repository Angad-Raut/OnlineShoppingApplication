package com.projectx.microservice.authenticationservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {
    private String token;
    private Date expiryDate;
}
