package com.softcafe.clinic_system.dto.report.doctor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Record with an overall report data of a doctor")
public record DoctorsReport(
        @Schema(description = "Primary key", example = "1")
        Long id,
        @Schema(description = "Full name", example = "John Doe")
        String name,
        @Schema(description = "Total appointments completed", example = "30")
        long completed,
        @Schema(description = "Total appointments cancelled", example = "3")
        long cancelled,
        @Schema(description = "Total amount earned by the doctor", example = "200000")
        Double revenue
) {
}
