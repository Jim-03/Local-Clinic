package com.softcafe.clinic_system.repositories;

import com.softcafe.clinic_system.entities.Billing;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Page<Billing> findByPatient(Patient patient, Pageable pageable);

    Page<Billing> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Billing> findByStatus(PaymentStatus status, Pageable pageable);

    Page<Billing> findByPaymentMethod(String method, Pageable pageable);
}
