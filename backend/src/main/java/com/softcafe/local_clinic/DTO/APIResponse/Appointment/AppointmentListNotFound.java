package com.softcafe.local_clinic.DTO.APIResponse.Appointment;

import com.softcafe.local_clinic.Entities.Appointment;
import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AppointmentListNotFound(
        @Schema(description = "The status of the response", example = "ERROR")
        Status status,
        @Schema(description = "The description of the response", example = "List not found")
        String message,
        @Schema(description = "An empty list", example = "null")
        List<Appointment> data
) {
}
