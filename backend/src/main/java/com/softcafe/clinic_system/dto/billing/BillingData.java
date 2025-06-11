package com.softcafe.clinic_system.dto.billing;

import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.entities.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record BillingData(
        Long id,
        PatientDto patient,
        Map<String, Double> bills,
        double totalAmount,
        String paymentMethod,
        double amountPaid,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
