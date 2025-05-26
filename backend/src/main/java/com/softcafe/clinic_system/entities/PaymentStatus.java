package com.softcafe.clinic_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Payment Status", description = "The status of the payment")
public enum PaymentStatus {
    PENDING,
    PARTIALLY_PAID,
    PAID,
    CANCELLED
}
