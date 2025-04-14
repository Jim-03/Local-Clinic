package com.softcafe.local_clinic.DTO.Patient;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The request body for searching for a patient")
public record SearchPatientDTO(
        @Schema(description = "Phone number", example = "0712345678")
        String phone,
        @Schema(description = "Email address", example = "johnDoe@example.com")
        String email,
        @Schema(description = "National ID card number", example = "123091")
        String cardNumber
) {
}
