package com.softcafe.local_clinic.DTO.Patient;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The request body for adding a new patient")
public record PatientDTO(
        @Schema(description = "Full legal name", example = "John Doe")
        String fullName,
        @Schema(description = "Date of birth in the format('yyyy-MM-dd", example = "2000-01-01")
        String dob,
        @Schema(description = "National Identity card number", example = "12345678")
        String nationalId,
        @Schema(description = "Phone number", example = "0712345678")
        String phone,
        @Schema(description = "Email address", example = "johnDoe@gmail.com")
        String email,
        @Schema(description = "The home address", example = "Town X ,K-street")
        String address,
        @Schema(description = "The name of the insurance provider", example = "NHIF")
        String insuranceProvider,
        @Schema(description = "Insurance number", example = "NHIF0101JD")
        String insuranceNumber,
        @Schema(description = "Blood group", example = "B - positive")
        String bloodType,
        @Schema(description = "The name if the patient's next of kin", example = "Jane Doe")
        String kinName,
        @Schema(description = "The next of kin's phone number", example = "0723456789")
        String kinContact,
        @Schema(description = "Patient's gender", example = "MALE")
        String gender) {

}
