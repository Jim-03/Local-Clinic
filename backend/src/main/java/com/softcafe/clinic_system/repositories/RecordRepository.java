package com.softcafe.clinic_system.repositories;

import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.Record;
import com.softcafe.clinic_system.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findByPatient(Patient patient, Pageable pageable);

    Page<Record> findByDoctor(Staff doctor, Pageable pageable);

    Page<Record> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
