package com.projectx.microservice.inventoryservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewStockDto {
    private Integer srNo;
    private Long inventoryId;
    private String categoryName;
    private String productName;
    private Integer totalCount;
    private Integer remainingCount;
    private Integer reservedCount;
}
