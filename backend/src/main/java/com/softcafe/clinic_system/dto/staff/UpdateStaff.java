package com.softcafe.clinic_system.dto.staff;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A DTO for updating staff data")
public record UpdateStaff(
        @Schema(description = "The existing password in readable format", example = "Th1sP4sw0!rd")
        String oldPassword,
        @Schema(description = "The newly updated data", implementation = NewStaff.class)
        NewStaff updatedData
) {
}
