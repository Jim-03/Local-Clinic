package com.softcafe.clinic_system.dto.staff;

import com.softcafe.clinic_system.entities.Gender;
import com.softcafe.clinic_system.entities.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "Staff Data", description = "An object containing the non-sensitive information of a staff member")
public record StaffData(
        @Schema(description = "The database's primary key", example = "1")
        Long id,
        @Schema(description = "The staff's full legal name", example = "John Doe")
        String fullName,
        @Schema(description = "The staff's email address", example = "johnDoe@example.com")
        String email,
        @Schema(description = "The staff's phone number", example = "0712345678")
        String phone,
        @Schema(description = "The staff's national ID card number", example = "14156366")
        String nationalId,
        @Schema(description = "The location the staff member resides", example = "Street X, Nakuru")
        String address,
        @Schema(description = "The date the user was born", example = "2000-11-20")
        LocalDate dateOfBirth,
        @Schema(description = "The staff's gender", implementation = Gender.class)
        Gender gender,
        @Schema(description = "The staff's profile image", implementation = File.class)
        File image,
        @Schema(description = "The user's availability", example = "true")
        String isActive,
        @Schema(description = "The role of the staff member", implementation = Role.class)
        Role role,
        @Schema(description = "The last date and time the account was accessed", example = "2025-05-24T10:41:56.976249081")
        LocalDateTime lastLogin,
        @Schema(description = "The date and time the record was created", example = "2025-05-24T10:41:56.976249081")
        LocalDateTime createdAt,
        @Schema(description = "The date and time the record was last updated", example = "2025-05-24T10:41:56.976249081")
        LocalDateTime updatedAt
) {
}
