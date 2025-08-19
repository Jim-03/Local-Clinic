package com.softcafe.clinic_system.dto.patient;

import com.softcafe.clinic_system.entities.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.File;
import java.time.LocalDate;

@Schema(name = "New Patient", description = "An object describing the structure of a new patient object")
public record NewPatient(
        @Schema(description = "The patient's full legal name", example = "John Doe")
        String fullName,
        @Schema(description = "The patient's email address", example = "johnDoe@example.com")
        String email,
        @Schema(description = "The patient's phone number", example = "0712345678")
        String phone,
        @Schema(description = "The patient's national ID card number", example = "14156366")
        String nationalId,
        @Schema(description = "The location the patient resides", example = "Street X, Nakuru")
        String address,
        @Schema(description = "The date the user was born", example = "2000-11-20")
        LocalDate dateOfBirth,
        @Schema(description = "The patient's gender", implementation = Gender.class)
        Gender gender,
        @Schema(description = "The phone number to the patient's next of kin", example = "0712345678")
        String emergencyContact,
        @Schema(description = "The name of the patient's next of kin", example = "Jane Doe")
        String emergencyName,
        @Schema(description = "The name of the insurance provider", example = "SHIF")
        String insuranceProvider,
        @Schema(description = "The patient's insurance number from the service provider", example = "SH3023")
        String insuranceNumber,
        @Schema(description = "The patient's blood group", example = "A+")
        String bloodType
) {
}
