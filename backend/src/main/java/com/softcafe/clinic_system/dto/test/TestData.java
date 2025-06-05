package com.softcafe.clinic_system.dto.test;

import com.softcafe.clinic_system.dto.record.RecordData;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;


import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "Lab Test", description = "Lab test data")
public record TestData(
        @Schema(description = "Primary key", example = "1")
        Long id,
        @Schema(description = "Record's data", implementation = RecordData.class)
        RecordData recordData,
        @ArraySchema(
                schema = @Schema(examples = {"urinalysis", "stool test", "blood test"}),
                arraySchema = @Schema(description = "Tests to be carried out")
        )
        List<String> investigations,
        @ArraySchema(
                schema = @Schema(examples = {"Pathogens present in urine", "Amoebiasis positive"}),
                arraySchema = @Schema(description = "Results from test")
        )
        List<String> findings,
        @Schema(description = "Date created", example = "2025-12-12T00:00:00")
        LocalDateTime createdAt,
        @Schema(description = "Date last updated", example = "2025-12-12T23:59:59")
        LocalDateTime updatedAt
) {
}
