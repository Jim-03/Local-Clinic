package com.softcafe.clinic_system.dto.staff;

import com.softcafe.clinic_system.entities.Gender;
import com.softcafe.clinic_system.entities.Role;
import com.softcafe.clinic_system.entities.StaffStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "New Staff", description = "An object describing the structure of a new staff object")
public record NewStaff(
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
        @Schema(description = "The staff account username", example = "johnD")
        String username,
        @Schema(description = "A string password", example = "Th1sI54PAs5W0R!D")
        String password,
        @Schema(description = "The user's availability", implementation = StaffStatus.class)
        StaffStatus staffStatus,
        @Schema(description = "The role of the staff member", implementation = Role.class)
        Role role,
        @Schema(description = "The last date and time the account was accessed", example = "2025-05-24T10:41:56.976249081")
        LocalDateTime lastLogin
        
) {
}
