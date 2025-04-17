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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient_records")
@Schema(name = "Patient record", description = "An entity that holds the data of each visit of a patient")
public class Record {
    @Id
    @GeneratedValue
    @Schema(description = "The primary key  of the record", example = "1")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", nullable = false)
    @Schema(description = "The patient's data", example = "{id: 1, fullName: \"John Doe\"}")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @Schema(description = "The doctor's data", example = "{id: 2, fullName: \"Jane Doe\"}")
    private Staff doctor;

    @Column(name = "reason_for_visit")
    @Schema(description = "The reason why the patient has visited", example = "Stomach ache")
    private String reason;

    @Column
    @Schema(description = "The symptoms mentioned by the patient", example = "Vomiting")
    private String symptoms;

    @Column(name = "physical_findings")
    @Schema(description = "The visible symptoms of the patient", example = "Dry skin")
    private String physicalSymptoms;

    @Column
    @Schema(description = "The diagnosis given by the doctor", example = "Typhoid")
    private String diagnosis;

    @Column
    @Schema(description = "Additional information given by the doctor", example = "Check up on 2025-12-12")
    private String notes;

    @Column
    @Schema(description = "The medication recommended by the doctor", example = "Flu Injection")
    private String medication;


    @Column
    @Schema(description = "The date the record was created", example = "2025-04-15T13:18:51.406807838")
    private LocalDateTime createdAt;
}
