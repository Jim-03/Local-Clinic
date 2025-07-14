package com.softcafe.clinic_system.repositories;

import com.softcafe.clinic_system.entities.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByStaff_Id(long id, Pageable pageable);
}
