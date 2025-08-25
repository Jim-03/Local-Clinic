package com.softcafe.clinic_system.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "New Appointment", description = "An object used to create new appointment data")
public record NewAppointment(
        @Schema(description = "Patient's primary key", example = "1")
        Long patientId,
        @Schema(description = "Doctor's primary key", example = "2")
        Long doctorId,
        @Schema(description = "The receptionist's primary key", example = "3")
        Long receptionistId
) {
}
