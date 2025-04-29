package com.softcafe.local_clinic.DTO.APIResponse.Appointment;

import com.softcafe.local_clinic.Entities.Appointment;
import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An appointment response record containing a status, message and list of appointments")
public record AppointmentListFound(
        @Schema(description = "The status of the response", example = "SUCCESS")
        Status status,
        @Schema(description = "The description of the response", example = "List of appointments found")
        String message,
        @ArraySchema(
                schema = @Schema(implementation = Appointment.class),
                arraySchema = @Schema(description = "List of incomplete appointments")
        )
        List<Appointment> data
) {
}
