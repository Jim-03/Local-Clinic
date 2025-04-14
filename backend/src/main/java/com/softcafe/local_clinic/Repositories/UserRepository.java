package com.softcafe.local_clinic.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softcafe.local_clinic.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmailAddress(String email);
    Optional<User> findByPhoneNumber(String phone);
    Optional<User> findByNationalId(String id);
}
