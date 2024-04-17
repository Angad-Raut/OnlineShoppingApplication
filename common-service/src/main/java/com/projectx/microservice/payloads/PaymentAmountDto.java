package com.projectx.microservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentAmountDto {
    @NotNull(message = "{paymentAmount.not.null}")
    private Double paymentAmount;
}
