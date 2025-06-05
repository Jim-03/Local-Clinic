package com.softcafe.clinic_system.dto.test;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object containing a list of tests and the total pages")
public record TestList(
        @Schema(description = "Total number of pages to expect", example = "3")
        int totalPages,
        @ArraySchema(
                schema = @Schema(implementation = TestData.class),
                arraySchema = @Schema(description = "Tests list")
        )
        List<TestData> tests
) {
}
