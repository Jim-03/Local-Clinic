package com.softcafe.local_clinic.Repositories;

import com.softcafe.local_clinic.Entities.Appointment;
import com.softcafe.local_clinic.Entities.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatus(AppointmentStatus appointmentStatus);

    List<Appointment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
