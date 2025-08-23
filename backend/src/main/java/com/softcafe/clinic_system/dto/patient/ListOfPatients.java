package com.softcafe.clinic_system.dto.patient;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object that contains a list of 10 patients and the total number of pages to be expected")
public record ListOfPatients(
        @Schema(description = "Total number of pages", example = "12")
        long totalPages,
        @ArraySchema(
                schema = @Schema(implementation = PatientDto.class),
                arraySchema = @Schema(description = "A list of patient data")
        )
        List<PatientDto> patients
) {
}
