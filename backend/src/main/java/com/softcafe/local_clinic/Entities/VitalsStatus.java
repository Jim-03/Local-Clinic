package com.softcafe.local_clinic.Entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The status of the patient's vitals record")
public enum VitalsStatus {
    @Schema(description = "The vitals data is complete")
    COMPLETE,
    @Schema(description = "Incomplete vitals data")
    INCOMPLETE
}
