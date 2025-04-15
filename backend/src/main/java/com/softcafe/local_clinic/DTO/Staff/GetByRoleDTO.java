package com.softcafe.local_clinic.DTO.Staff;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Get staff by role object", description = "An object that is used to find a list of staff members with the same role")
public record GetByRoleDTO(
        @Schema(name = "role", description = "The role of the staff member", example = "DOCTOR")
        String role,
        @Schema(name = "page", description = "The page number", example = "3")
        int page
) {
}
