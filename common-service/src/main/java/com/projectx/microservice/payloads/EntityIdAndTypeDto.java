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
public class EntityIdAndTypeDto {
    @NotNull(message = "{entityId.not.null}")
    private Long entityId;
    @NotNull(message = "{entityType.not.null}")
    private Integer entityType;
}
