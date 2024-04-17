package com.projectx.microservice.categoryservice.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    @NotNull(message = "{category.name.not.null}")
    private String categoryName;
    @NotNull(message = "{category.description.not.null}")
    private String description;
}
