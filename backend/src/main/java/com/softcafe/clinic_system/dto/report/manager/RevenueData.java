package com.softcafe.clinic_system.dto.report.manager;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Record containing revenue data")
public record RevenueData(
        @Schema(description = "The total amount earned", example = "3000000")
        double total,
        @Schema(description = "The total amount that is to be collected", example = "3500000")
        double expected
) {
}
