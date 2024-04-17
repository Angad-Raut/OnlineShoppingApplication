package com.projectx.microservice.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_details")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    private String userEmail;
    private Long userMobile;
    private String userPassword;
    private String userRole;
    private String gender;
    private Date insertedTime;
    private Date updatedTime;
    private Boolean isAdmin;
    private Boolean userStatus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

    public Users(Users users) {
        this.userId = users.userId;
        this.userName= users.userName;
        this.userMobile = users.userMobile;
        this.userEmail = users.userEmail;
        this.gender = users.gender;
        this.userPassword = users.userPassword;
        this.userRole = users.userRole;
        this.isAdmin = users.isAdmin;
        this.userStatus = users.userStatus;
        this.userAddress = users.userAddress;
        this.insertedTime = users.insertedTime;
        this.updatedTime = users.updatedTime;
    }

    public List<String> getUserRoles() {
        return Collections.singletonList(userRole);
    }
}
