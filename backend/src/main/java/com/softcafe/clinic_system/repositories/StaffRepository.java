package com.softcafe.clinic_system.repositories;

import com.softcafe.clinic_system.entities.Role;
import com.softcafe.clinic_system.entities.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Page<Staff> findByRole(Role role, Pageable page);
}
