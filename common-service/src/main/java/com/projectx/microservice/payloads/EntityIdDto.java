package com.projectx.microservice.payloads;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityIdDto {
    @NotNull(message = "{entityId.not.null}")
    private Long entityId;
}
