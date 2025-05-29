package com.softcafe.clinic_system.dto.staff;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object containing a list of staff members")
public record ListOfStaff(
        @Schema(description = "The number of pages to expect", example = "5")
        int totalPages,
        @ArraySchema(
                schema = @Schema(implementation = NewStaff.class)
        )
        @Schema(description = "A list of staff members")
        List<StaffData> staffList
) {
}
