package com.softcafe.local_clinic.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "national_id", unique = true)
    private String nationalId;

    @Column(length = 100)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email_address", unique = true, length = 50)
    private String emailAddress;

    @Column(name = "phone_number", unique = true, nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Gender gender;

    @Column(name = "home_address")
    private String address;
}
