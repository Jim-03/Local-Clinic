package com.softcafe.clinic_system.dto.staff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Staffs credentials for logging in")
public record StaffCredentials(
        @Schema(description = "Account username", example = "johnDoe")
        String username,
        @NotNull
        @Schema(description = "Account's password", example = "A5ecurEP4s5w0!d")
        String password,
        @Schema(description = "Account's email", example = "john.doe@example.com")
        String email,
        @Schema(description = "User's phone number", example = "+254712345678")
        String phone
) {
}
