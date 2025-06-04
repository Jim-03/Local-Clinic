package com.softcafe.clinic_system.dto.record;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "New Record", description = "An object containing a new record's details")
public record NewRecord(
        @Schema(description = "Patient's primary key", example = "1")
        @NotNull(message = "Provide the patient's ID!")
        @Min(value = 1, message = "ID must be at least 1")
        Long patientId,
        @Schema(description = "Doctor's primary key", example = "2")
        @NotNull(message = "Provide the doctor's ID!")
        @Min(value = 1, message = "ID must be at least 1")
        Long doctorId,
        @Schema(description = "Reason of visit", example = "Frequent headaches and diarrhea")
        @NotNull(message = "Provide the reason for visit")
        @Size(max = 255, message = "Reason shouldn't exceed 255 characters")
        String reason,
        @ArraySchema(
                schema = @Schema(example = "Pale eyes"),
                arraySchema = @Schema(description = "Visible symptoms")
        )
        @NotNull(message = "Provide the patient's symptoms")
        List<@Size(max = 15, message = "Symptoms shouldn't exceed 15") String> symptoms,
        @Schema(description = "The doctor's diagnosis", example = "Typhoid")
        String diagnosis,
        @ArraySchema(
                schema = @Schema(example = "Pain killers 500mg"),
                arraySchema = @Schema(description = "Treatments to be administered")
        )
        List<@Size(max = 20, message = "Max treatment is 20") String> treatment,
        @ArraySchema(
                schema = @Schema(example = "Checkup on 2025-12-12"),
                arraySchema = @Schema(description = "Extra notes provided by the doctor")
        )
        List<@Size(max = 10, message = "Maximum notes is 10") String> notes
) {}
