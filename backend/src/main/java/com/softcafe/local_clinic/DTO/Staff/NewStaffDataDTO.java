package com.softcafe.local_clinic.DTO.Staff;

import com.softcafe.local_clinic.Entities.StaffRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Staff data", description = "An object containing a user's data")
public record NewStaffDataDTO(
        @Schema(description = "The account username", example = "johnD")
        String username,
        @Schema(description = "The account's password", example = "securEP4swor!d")
        String password,
        @Schema(description = "Full legal name", example = "John Doe")
        String fullName,
        @Schema(description = "Date of birth in the format('yyyy-MM-dd", example = "2000-01-01")
        String dob,
        @Schema(description = "The role of the user in the hospital", example = "DOCTOR")
        StaffRole role,
        @Schema(description = "National Identity card number", example = "12345678")
        String nationalId,
        @Schema(description = "Phone number", example = "0712345678")
        String phone,
        @Schema(description = "Email address", example = "johnDoe@gmail.com")
        String email,
        @Schema(description = "The home address", example = "Town X ,K-street")
        String address,
        @Schema(description = "user's gender", example = "MALE")
        String gender,
        @Schema(description = "The department of the staff", example = "Outpatient")
        String department,
        @Schema(description = "The user's area of specialization", example = "Cardiology")
        String specialization,
        @Schema(description = "Is the staff on active duty", example = "false")
        boolean isActive
) {}
