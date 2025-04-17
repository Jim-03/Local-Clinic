package com.softcafe.local_clinic.Repositories;

import com.softcafe.local_clinic.Entities.Record;
import com.softcafe.local_clinic.Entities.Vitals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VitalsRepository extends JpaRepository<Vitals, Long> {
    Optional<Vitals> findByRecord(Optional<Record> record);
}
