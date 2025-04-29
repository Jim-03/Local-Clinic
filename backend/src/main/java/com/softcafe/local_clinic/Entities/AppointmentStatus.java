package com.softcafe.local_clinic.Entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Appointment status", description = "The different states of an appointment")
public enum AppointmentStatus {
    @Schema(description = "The appointment doesn't have the patient's vitals")
    MISSING_VITALS,
    @Schema(description = "The appointment doesn't have investigations such as lab tests")
    MISSING_INVESTIGATIONS,
    @Schema(description = "The appointment wasn't fully completed")
    INCOMPLETE,
    @Schema(description = "The appointment was completed successfully")
    COMPLETE
}
