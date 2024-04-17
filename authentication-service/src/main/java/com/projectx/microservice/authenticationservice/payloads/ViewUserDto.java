package com.projectx.microservice.authenticationservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewUserDto {
    private Integer srNo;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long userMobile;
    private String gender;
    private String userStatus;
}
