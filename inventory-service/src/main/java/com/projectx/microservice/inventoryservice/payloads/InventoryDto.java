package com.projectx.microservice.inventoryservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDto {
    private Long id;
    @NotNull(message = "{categoryId.not.null}")
    private Long categoryId;
    @NotNull(message = "{productId.not.null}")
    private Long productId;
    @NotNull(message = "{quantity.not.null}")
    private Integer quantity;
}
