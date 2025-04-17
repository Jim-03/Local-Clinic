package com.softcafe.local_clinic.DTO.Record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "New patient record data", description = "An object containing the data to a patient's record")
public record NewRecordDTO(
        @Schema(description = "The patient's primary key", example = "1")
        Long patientId,
        @Schema(description = "The doctor's primary key", example = "2")
        Long staffId,
        @Schema(description = "The reason for the visit", example = "Checkup")
        String reason,
        @Schema(description = "The patient's description", example = "Stomach ache, exhaustion")
        String symptoms,
        @Schema(description = "The visible symptoms give by the doctor", example = "pale skin, pale eyes")
        String physicalSymptoms,
        @Schema(description = "The doctor's findings", example = "stomach flu")
        String diagnosis,
        @Schema(description = "Additional notes by the doctor", example = "Should return for a checkup")
        String notes,
        @Schema(description = "The prescription", example = "Take flu injection, drink more water")
        String medication
){
}
