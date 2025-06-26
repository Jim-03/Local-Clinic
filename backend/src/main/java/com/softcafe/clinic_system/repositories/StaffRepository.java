package com.softcafe.clinic_system.repositories;

import com.softcafe.clinic_system.entities.Role;
import com.softcafe.clinic_system.entities.Staff;
import com.softcafe.clinic_system.entities.StaffStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Page<Staff> findByRole(Role role, Pageable page);

    Optional<Staff> findByUsernameOrPhoneOrEmail(String username, String phone, String email);

    Page<Staff> findByStatus(StaffStatus staffStatus, Pageable pageable);

    Optional<Staff> findByEmail(String value);

    Optional<Staff> findByPhone(String value);

    Optional<Staff> findByNationalId(String id);

    Optional<Staff> findByUsername(String username);

    Page<Staff> findByFullNameContainingIgnoreCase(String value, Pageable pageable);
}
