package com.projectx.microservice.authenticationservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private Long addressId;
    private BillingAddress billingAddress;
    private ShippingAddress shippingAddress;
}
