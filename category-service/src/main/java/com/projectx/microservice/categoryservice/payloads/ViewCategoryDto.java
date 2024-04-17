package com.projectx.microservice.categoryservice.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewCategoryDto {
    private Integer srNo;
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryStatus;
}
