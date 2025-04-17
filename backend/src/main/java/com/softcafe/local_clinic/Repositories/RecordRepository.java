package com.softcafe.local_clinic.Repositories;

import com.softcafe.local_clinic.Entities.Patient;
import com.softcafe.local_clinic.Entities.Record;
import com.softcafe.local_clinic.Entities.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByPatient(Optional<Patient> patient);

    List<Record> findByDoctor(Staff staff, Pageable page);

    List<Record> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable page);
}
