package com.softcafe.local_clinic.DTO.Staff;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "Authorization object",
        description = "An object containing the credentials used to log in to the system"
)
public record AuthorizationDTO(
        @Schema(description = "Account username", example = "johnD")
        String username,
        @Schema(description = "The user's work email", example = "johnD@hospital.ac.org")
        String email,
        @Schema(description = "The user's phone number", example = "0712345678")
        String phone,
        @Schema(description = "The account's password", example = "securEp4swo!d")
        String password
) {
}
