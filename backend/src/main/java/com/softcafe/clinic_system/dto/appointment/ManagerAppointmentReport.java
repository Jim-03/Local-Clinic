package com.softcafe.clinic_system.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Object containing appointments data for a report")
public record ManagerAppointmentReport(
        @Schema(description = "Total number of appointments", example = "300")
        long total,
        @Schema(description = "Total completed appointments", example = "259")
        long completed,
        @Schema(description = "Total incomplete appointments", example = "40")
        long pending,
        @Schema(description = "Total cancelled appointments", example = "1")
        long cancelled
) {
}
