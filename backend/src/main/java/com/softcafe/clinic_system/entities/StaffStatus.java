package com.softcafe.clinic_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "An enum to check the availability of a staff member")
public enum StaffStatus {
    ON_DUTY,
    OFF,
    SUSPENDED
}
