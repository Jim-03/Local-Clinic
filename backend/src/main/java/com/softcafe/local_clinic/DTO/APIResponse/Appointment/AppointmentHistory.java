package com.softcafe.local_clinic.DTO.APIResponse.Appointment;

import com.softcafe.local_clinic.Entities.Appointment;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Appointment history")
public record AppointmentHistory(
        @ArraySchema(
                schema = @Schema(implementation = Appointment.class),
                arraySchema = @Schema(description = "A list of appointments")
        )
        List<Appointment> appointments,
        @Schema(description = "The total number of pages to expect", example = "3")
        int totalPages
) {
}
