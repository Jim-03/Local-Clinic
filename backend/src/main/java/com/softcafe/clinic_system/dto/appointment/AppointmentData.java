package com.softcafe.clinic_system.dto.appointment;

import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.dto.staff.StaffData;
import com.softcafe.clinic_system.entities.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "Appointment data", description = "Details of an appointment")
public record AppointmentData(
        @Schema(description = "Primary key", example = "1")
        Long id,
        @Schema(description = "Patient's details", implementation = PatientDto.class)
        PatientDto patient,
        @Schema(description = "Doctor's details", implementation = StaffData.class)
        StaffData doctor,
        @Schema(description = "Completeness of the appointment", implementation = AppointmentStatus.class)
        AppointmentStatus status,
        @Schema(description = "Date and time of creation", implementation = LocalDateTime.class)
        LocalDateTime createdAt,
        @Schema(description = "Date and time last updated", implementation = LocalDateTime.class)
        LocalDateTime updatedAt
) {
}
