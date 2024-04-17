package com.projectx.microservice.orderservice.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    @NotNull(message = "{customerid.not.null}")
    private Long customerId;
    @NotNull(message = "{orderamount.not.null}")
    private Double orderAmount;
    private Double discountAmount;
    private Integer discountType;
    @NotEmpty(message = "{itemList.empty}")
    private Set<OrderItemDto> itemDtoSet = new HashSet<>();
}
