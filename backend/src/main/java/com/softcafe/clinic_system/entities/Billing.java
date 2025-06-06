package com.softcafe.clinic_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "billings")
public class Billing {
    @Id
    @GeneratedValue
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @Schema(description = "Patient's details", implementation = Patient.class)
    private Patient patient;

    @ElementCollection
    @MapKeyColumn(name = "bill_type")
    @CollectionTable(name = "bills", joinColumns = @JoinColumn(name = "billing_id"))
    @Column(name = "bill_amount")
    @Schema(description = "A mapped string double object", example = "{\"consultation\": 3000.00, \"pharmacy\": 20,000.00}")
    private Map<String, Double> bills;

    @Column(name = "total_amount")
    @Schema(description = "Total amount", example = "300,000.00")
    private double totalAmount;

    @Column(name = "payment_method")
    @Schema(description = "The mode of payment", example = "card")
    private String paymentMethod;

    @Column(name = "amount_paid")
    @Schema(description = "Amount already paid", example = "50,000.00")
    private double amountPaid;

    @Enumerated(value = EnumType.STRING)
    @Column
    @Schema(description = "Payment status", implementation = PaymentStatus.class)
    private PaymentStatus status;

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
