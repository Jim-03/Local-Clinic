package com.softcafe.clinic_system.entities;

import io.swagger.oas.annotations.media.Schema;

@Schema(description = "The roles staff members have in the system")
public enum Role {
    NURSE,
    RECEPTIONIST,
    DOCTOR,
    PHARMACIST,
    TECHNICIAN
}
