package com.softcafe.clinic_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The state of an appointment")
public enum AppointmentStatus {
    PENDING,
    COMPLETE,
    INCOMPLETE
}
