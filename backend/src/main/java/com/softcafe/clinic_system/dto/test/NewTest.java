package com.softcafe.clinic_system.dto.test;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "New Lab Test", description = "Object containing the data for a new lab test")
public record NewTest(
        @Schema(description = "Record's primary key", example = "1")
        @NotNull(message = "Provide the record's ID") @Min(value = 1, message = "ID should be at least 1!")
        Long recordId,
        @ArraySchema(
                schema = @Schema(examples = {"urinalysis", "stool test", "blood test"}),
                arraySchema = @Schema(description = "Tests to be carried out")
        )
        @NotNull(message = "Provide the investigations to be done")
        List<@Size(max = 10, message = "Maximum investigations are 10!") String> investigations,
        @ArraySchema(
                schema = @Schema(examples = {"Pathogens present in urine", "Amoebiasis positive"}),
                arraySchema = @Schema(description = "Results from test")
        )
        List<String> findings
) {
}
