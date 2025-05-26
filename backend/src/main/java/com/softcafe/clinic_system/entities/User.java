package com.softcafe.clinic_system.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Schema(name = "user", description = "The base class for all users of the system")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Schema(description = "The database's primary key", example = "1")
    private Long id;

    @Column(name = "full_name", nullable = false)
    @Schema(description = "The user's full legal name", example = "John Doe")
    private String fullName;

    @Column(name = "email_address", unique = true)
    @Schema(description = "The user's email address", example = "johnDoe@example.com")
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    @Schema(description = "The user's phone number", example = "0712345678")
    private String phone;

    @Column(name = "national_id_number", unique = true, nullable = false)
    @Schema(description = "The user's national ID card number", example = "14156366")
    private String nationalId;

    @Column(name = "address", nullable = false)
    @Schema(description = "The location the user resides", example = "Street X, Nakuru")
    private String address;

    @Column(name = "date_of_birth", nullable = false)
    @Schema(description = "The date the user was born", example = "2000-11-20")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The user's gender", example = "MALE")
    private Gender gender;

    @Column(name = "profile_image")
    @Schema(description = "The user's profile image", example = "profile.jpg")
    private String image;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "The date and time the record was created", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @Schema(description = "The date and time the record was last updated", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime updatedAt;
}