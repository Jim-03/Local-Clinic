package com.softcafe.clinic_system.entities;

import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.dto.staff.StaffData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
@Schema(description = "A class representing the details of an appointment between a patient and a doctor")
public class Appointment {

    @Id
    @GeneratedValue
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @Schema(description = "Patient's details", implementation = PatientDto.class)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @Schema(description = "Doctor's details", implementation = StaffData.class)
    private Staff doctor;

    @ManyToOne
    @JoinColumn(name = "receptionist_id", nullable = false)
    private Staff receptionist;

    @Column
    @Schema(description = "The completeness", example = "PENDING")
    private AppointmentStatus status;

    @Column(name = "created_at")
    @Schema(description = "The date and time created", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "The date and time last updated", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime updatedAt;

    @PrePersist
    private void create() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void update() {
        this.updatedAt = LocalDateTime.now();
    }
}
