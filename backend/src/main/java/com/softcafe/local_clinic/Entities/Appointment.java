package com.softcafe.local_clinic.Entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
@Schema(name = "Appointment model", description = "A model containing the information of an appointment between a patient and doctor")
public class Appointment {
    @Id
    @GeneratedValue
    @Schema(description = "The appointment's primary key", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @Schema(description = "The patients details", implementation = Patient.class)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @Schema(description = "The doctor's details", implementation = Staff.class)
    private Staff doctor;

    @Column(name = "created_at")
    @Schema(description = "The data and time the appointment was created", implementation = LocalDateTime.class)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The state of the appointment", example = "COMPLETE")
    private AppointmentStatus status;

}
