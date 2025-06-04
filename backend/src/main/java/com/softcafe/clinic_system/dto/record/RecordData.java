package com.softcafe.clinic_system.dto.record;

import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.dto.staff.StaffData;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Object containing a record's data from the database")
public record RecordData(
        @Schema(description = "Primary key", example = "1")
        Long id,
        @Schema(description = "Patient's details", implementation = PatientDto.class)
        PatientDto patient,
        @Schema(description = "Doctor's details", implementation = StaffData.class)
        StaffData doctor,
        @Schema(description = "Reason for visitation", example = "Frequent headaches and diarrhea")
        String reason,
        @ArraySchema(
                schema = @Schema(example = "Pale eyes"),
                arraySchema = @Schema(description = "Visible symptoms")
        )
        List<String> symptoms,
        @Schema(description = "Doctor's findings", example = "Typhoid")
        String diagnosis,
        @ArraySchema(
                schema = @Schema(example = "Pain killers 500mg"),
                arraySchema = @Schema(description = "Treatment/medical plans")
        )
        List<String> treatment,
        @ArraySchema(
                schema = @Schema(example = "Checkup on 2025-12-12"),
                arraySchema = @Schema(description = "Reviewing doctor's notes")
        )
        List<String> notes,
        @Schema(description = "Date record was created", implementation = LocalDateTime.class)
        LocalDateTime createdAt,
        @Schema(description = "Date record was updated", implementation = LocalDateTime.class)
        LocalDateTime updatedAt
) {
}
