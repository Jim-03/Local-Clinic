package com.softcafe.clinic_system.entities;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "staff")
@Schema(description = "The staff class inheriting from user")
public class Staff extends User {
    @Column(nullable = false, unique = true)
    @Schema(description = "The staff account username", example = "johnD")
    private String username;

    @Column(nullable = false)
    @Schema(description = "A hashed string password", example = "Th1sI54PAs5W0R!D")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "The user's availability", implementation = StaffStatus.class)
    private StaffStatus status;

    @Column(nullable = false)
    @Schema(description = "The role of the staff member", implementation = Role.class)
    private Role role;

    @Column(nullable = false)
    @Schema(description = "The last date and time the account was accessed", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime lastLogin;
}
