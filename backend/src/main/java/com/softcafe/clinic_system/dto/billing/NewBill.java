package com.softcafe.clinic_system.dto.billing;

import com.softcafe.clinic_system.entities.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public record NewBill(
        @Schema(description = "Patient's details", example = "1")
        @Min(value = 1, message = "ID should be at least 1!") @NotNull(message = "Provide the patient's ID!")
        Long patientId,
        @Schema(description = "A mapped string-double object of bills", example = "{\"stool extraction\": 10,000}")
        Map<String, Double> bills,
        @Schema(description = "Method paid", example = "cash")
        String paymentMethod,
        @Schema(description = "Total amount paid", example = "250,000")
        Double amountPaid,
        @Schema(description = "Payment status", implementation = PaymentStatus.class)
        PaymentStatus status
) {
}
