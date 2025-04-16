package com.softcafe.local_clinic.DTO.Patient;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "The response body for fetching a patient's data")
public record PatientDataDTO(
        @Schema(description = "The patient's primary key", example = "1")
        Long id,

        @Schema(description = "Full legal name", example = "John Doe")
        String fullName,

        @Schema(description = "Phone number", example = "0712345678")
        String phone,
        @Schema(description = "Email address", example = "johnDoe@example.com")
        String email,
        @Schema(description = "The national ID card number", example = "1241143₃")
        String nationalId,
        @Schema(description = "Date of birth", example = "2000-01-01")
        LocalDate dateOfBirth,
        @Schema(description = "Home address", example = "Town X, k-street")
        String address,
        @Schema(description = "Blood group", example = "B - positive")
        String bloodType,
        @Schema(description = "Allergies", example = "Gluten")
        String allergies,
        /*@Schema(description = "Name of insurance provider", example = "NHIF")
        String insuranceProvider,
        @Schema(description = "Insurance number", example = "NHIF0101JD")
        String insuranceNumber,*/
        @Schema(description = "The name of the next of kin", example = "Jane Doe")
        String kinName,
        @Schema(description = "Next of kin's phone number", example = "0723456789")
        String kinContact
) {}
