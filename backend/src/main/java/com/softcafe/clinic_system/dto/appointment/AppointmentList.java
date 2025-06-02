package com.softcafe.clinic_system.dto.appointment;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object containing the list of appointments and the total number of pages to be expected")
public record AppointmentList(
        @Schema(description = "Total pages to expect", example = "3")
        int totalPages,
        @ArraySchema(
                schema = @Schema(implementation = AppointmentData.class),
                arraySchema = @Schema(description = "A list of appointments")
        )
        List<AppointmentData> appointments
) {
}
