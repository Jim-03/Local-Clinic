package com.softcafe.clinic_system.dto.report;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "A record object that's used to retrieves a Log's details")
public record LogData(
        @Schema(description = "Primary key", example = "1")
        Long id,
        @Schema(description = "The action the log involved", example = "Staff data with ID:1 was deleted")
        String action,
        @Schema(description = "THe date abd time the log was created", example = "2025-05-17T10:00:00")
        LocalDateTime time
) {
}
