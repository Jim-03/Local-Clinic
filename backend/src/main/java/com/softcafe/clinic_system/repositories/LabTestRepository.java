package com.softcafe.clinic_system.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softcafe.clinic_system.entities.LabTest;
import com.softcafe.clinic_system.entities.Record;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    List<LabTest> findAllByRecord(Record record);

    Page<LabTest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
