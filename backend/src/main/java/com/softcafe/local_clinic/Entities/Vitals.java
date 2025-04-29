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
@Table(name = "vital_records")
@Schema(name = "Patient vitals", description = "The vital measurements of a patient")
public class Vitals {
    @Id
    @GeneratedValue
    @Schema(description = "The primary key", example = "1")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "record_id", nullable = false)
    @Schema(description = "The record this measurements belong to", example = "{\"id\": 123, \"patient\": {id: 1, fullName: \"John Doe\"}}")
    private Record record;

    @Column(name = "body_temperature", nullable = false)
    @Schema(description = "The patient's body temperature", example = "36")
    private Double bodyTemperature;

    @Column(name = "height_in_centimeters", nullable = false)
    @Schema(description = "The patient's height", example = "180")
    private int height;

    @Column(name = "mass_in_kilograms", nullable = false)
    @Schema(description = "The patient's mass in kilograms", example = "65")
    private Double mass;

    @Column(name = "heart_rate", nullable = false)
    @Schema(description = "The beats per minute", example = "72")
    private int heartRate;

    @Column(name = "systolic_number", nullable = false)
    @Schema(description = "The systolic number used for the blood pressure", example = "120")
    private int systolic;

    @Column(name = "diastolic_number", nullable = false)
    @Schema(description = "The diastolic number used for the blood pressure", example = "88")
    private int diastolic;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The completeness of the vitals data", example = "INCOMPLETE")
    private VitalsStatus status;

    @Column(name = "created_at")
    @Schema(description = "The date and time the vitals data was created")
    private LocalDateTime createdAt;
}
