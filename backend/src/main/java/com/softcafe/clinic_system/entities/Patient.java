package com.softcafe.clinic_system.entities;

import io.swagger.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "patients")
public class Patient extends User{
    @Column(name = "emergency_contact", nullable = false)
    @Schema(description = "The phone number to the patient's next of kin", example = "0712345678")
    private String emergencyContact;

    @Column(name = "emergency_name", nullable = false)
    @Schema(description = "The name of the patient's next of kin", example = "Jane Doe")
    private String emergencyName;

    @Column(name = "insurance_provider", nullable = false)
    @Schema(description = "The name of the insurance provider", example = "SHIF")
    private String insuranceProvider;

    @Column(name = "insurance_number", nullable = false, unique = true)
    @Schema(description = "The patient's insurance number from the service provider", example = "SH3023")
    private String insuranceNumber;

    @Column(name = "blood_type")
    private String bloodType;
}
