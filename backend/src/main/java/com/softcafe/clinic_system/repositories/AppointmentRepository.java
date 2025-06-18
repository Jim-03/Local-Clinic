package com.softcafe.clinic_system.repositories;

import com.softcafe.clinic_system.entities.Appointment;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByDoctor(Pageable pageable, Staff staff);

    Page<Appointment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable of);

    Page<Appointment> findByPatient(Patient patient, Pageable pageable);
}