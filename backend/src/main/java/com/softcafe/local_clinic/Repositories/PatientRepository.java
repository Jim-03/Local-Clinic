package com.softcafe.local_clinic.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softcafe.local_clinic.Entities.Patient;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
