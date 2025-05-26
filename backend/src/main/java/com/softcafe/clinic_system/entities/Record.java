package com.softcafe.clinic_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient_records")
@Schema(name = "Record", description = "An object representing the patient's record on a single visit")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    @Schema(description = "Patient's details", implementation = Patient.class)
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    @Schema(description = "Doctor's details", implementation = Staff.class)
    private Staff doctor;

    @Column(name = "reason_for_visiting")
    @Schema(description = "Reason for visit", example = "Check-up")
    private String reason;

    @ElementCollection
    @CollectionTable(name = "record_symptoms", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "symptom")
    @Schema(description = "List of visible symptoms", example = "[\"red eyes\", \"swollen eye\"]")
    private List<String> symptoms;

    @Column
    @Schema(description = "Diagnosis", example = "Cancer")
    private String diagnosis;

    @ElementCollection
    @CollectionTable(name = "record_treatments", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "treatment")
    @Schema(description = "Treatments given", example = "[\"chemotherapy\"]")
    private List<String> treatment;

    @ElementCollection
    @CollectionTable(name = "record_notes", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "note")
    @Schema(description = "Notes for next review", example = "[\"Should have completed medicine X\"]")
    private List<String> notes;

    @Column(name = "created_at")
    @Schema(description = "Creation date", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Date updated", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
