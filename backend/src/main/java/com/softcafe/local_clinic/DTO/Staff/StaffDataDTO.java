package com.softcafe.local_clinic.DTO.Staff;

import com.softcafe.local_clinic.Entities.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "Staff data", description = "An object containing the staff member's non-sensitive data")
public record StaffDataDTO(
        @Schema(description = "The user's database ID", example = "2")
        Long id,
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
        @Schema(description = "user's gender", example = "MALE")
        Gender gender,
        @Schema(description = "The department of the staff", example = "Outpatient")
        String department,
        @Schema(description = "The user's area of specialization", example = "Cardiology")
        String specialization,
        @Schema(description = "Is the staff on active duty", example = "false")
        boolean isActive,
        @Schema(description = "The last time the staff member accessed their account", example = "2025-04-15T13:18:51.406807838")
        LocalDateTime lastLogin,
        @Schema(description = "The date the account was created", example = "2025-04-15")
        LocalDate employmentDate
) {}
