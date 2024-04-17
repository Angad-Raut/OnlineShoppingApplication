package com.projectx.microservice.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_address")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String billingStreet;
    private String billingCity;
    private String billingState;
    private Integer billingPinCode;
    private String shippingStreet;
    private String shippingCity;
    private String shippingState;
    private Integer shippingPinCode;
}
