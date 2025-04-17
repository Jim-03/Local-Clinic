package com.softcafe.local_clinic.DTO.Vitals;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Vitals", description = "Object containing the vitals data")
public record NewVitals(
        @Schema(description = "The record this measurements belong to", example = "{\"id\": 123, \"patient\": {id: 1, fullName: \"John Doe\"}}")
        Long recordId,
        @Schema(description = "The patient's body temperature", example = "36")
        Double temperature,
        @Schema(description = "The patient's height in centimeters", example = "180")
        int height,
        @Schema(description = "The patient's mass in kilograms", example = "65")
        Double mass,
        @Schema(description = "The beats per minute", example = "72")
        int heartRate,
        @Schema(description = "The systolic number used for the blood pressure", example = "120")
        int systolicNumber,
        @Schema(description = "The diastolic number used for the blood pressure", example = "88")
        int diastolicNumber
) {
}
