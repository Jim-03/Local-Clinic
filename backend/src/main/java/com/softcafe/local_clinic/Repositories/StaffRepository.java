package com.softcafe.local_clinic.Repositories;

import com.softcafe.local_clinic.Entities.Staff;
import com.softcafe.local_clinic.Entities.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
    List<Staff> findByRole(StaffRole role, Pageable page);
    List<Staff> findBySpecialization(String specialization);
}
