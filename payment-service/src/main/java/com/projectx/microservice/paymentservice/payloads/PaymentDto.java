package com.projectx.microservice.paymentservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private Long paymentId;
    @NotNull(message = "{orderId.not.null}")
    private Long orderId;
    @NotNull(message = "{paymentAmount.not.null}")
    private Double paymentAmount;
    private String razorpayOrderId;
    private String razorpayPaymentId;
}
