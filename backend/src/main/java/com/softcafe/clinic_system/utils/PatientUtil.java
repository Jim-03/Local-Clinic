package com.softcafe.clinic_system.utils;

import com.softcafe.clinic_system.dto.patient.NewPatient;
import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.entities.Patient;

import java.time.LocalDateTime;
import java.util.Objects;

public class PatientUtil {

    /**
     * Checks if the patient data is correct
     * @param dto The new patient's data
     * @throws IllegalArgumentException in case of invalid data
     */
    public static void validate(NewPatient dto) {
        if (dto.fullName() == null) {
            throw new IllegalArgumentException("Provide patient's full name!");
        }

        if (dto.address() == null) {
            throw new IllegalArgumentException("Provide patient's location address!");
        }

        if (dto.phone() == null) {
            throw new IllegalArgumentException("Provide patient's phone number!");
        } else {
            if (!Util.isValidPhone(dto.phone())) {
                throw new IllegalArgumentException("Provide a valid phone number!");
            }
        }

        if (dto.email() != null) {
            if (!Util.isValidEmail(dto.email())) {
                throw new IllegalArgumentException("Provide a valid email address!");
            }
        }

        if (dto.nationalId() == null) {
            throw new IllegalArgumentException("Provide a national ID card number!");
        }

        if (dto.dateOfBirth() == null) {
            throw new IllegalArgumentException("Provide the date of birth!");
        }

        if (dto.gender() == null) {
            throw new IllegalArgumentException("Provide the patient's gender!");
        } else {
            if (!Objects.equals(java.lang.String.valueOf(dto.gender()), "MALE") && !Objects.equals(java.lang.String.valueOf(dto.gender()), "FEMALE")) {
                throw new IllegalArgumentException("Provide a valid gender!");
            }
        }

        if (dto.emergencyContact() == null) {
            throw new IllegalArgumentException("Provide the contact of the next of kin!");
        } else {
            if (!Util.isValidPhone(dto.emergencyContact())) {
                throw new IllegalArgumentException("Provide a valid phone number for the next of kin!");
            }
        }

        if (dto.emergencyName() == null) {
            throw new IllegalArgumentException("Provide the name of the next of kin!");
        }

        if (dto.insuranceNumber() == null) {
            throw new IllegalArgumentException("Provide the patient's insurance number!");
        }

        if (dto.insuranceProvider() == null) {
            throw new IllegalArgumentException("Provide the name of the insurance provider!");
        }
    }

    /**
     * Converts the new patient DTO to a patient object
     * @param dto The new patient data
     * @return The new patient object
     */
    public static Patient toPatient(NewPatient dto) {
        Patient patient = new Patient();
        patient.setFullName(dto.fullName().trim());
        patient.setBloodType(dto.bloodType());
        patient.setEmergencyContact(dto.emergencyContact());
        patient.setEmergencyName(dto.emergencyName());
        patient.setInsuranceNumber(dto.insuranceNumber());
        patient.setInsuranceProvider(dto.insuranceProvider());
        patient.setAddress(dto.address());
        patient.setCreatedAt(LocalDateTime.now());
        patient.setDateOfBirth(dto.dateOfBirth());
        patient.setEmail(dto.email().toLowerCase().trim());
        patient.setGender(dto.gender());
        patient.setNationalId(dto.nationalId().trim());
        patient.setPhone(dto.phone().trim());
        patient.setUpdatedAt(LocalDateTime.now());
        return patient;
    }

    /**
     * Updates a patient's data from a DTO
     * @param patient Patient's data
     * @param newData The new data
     */
    public static void updatePatient(Patient patient, NewPatient newData) {
        patient.setFullName(newData.fullName().trim());
        patient.setBloodType(newData.bloodType());
        patient.setEmergencyContact(newData.emergencyContact());
        patient.setEmergencyName(newData.emergencyName());
        patient.setInsuranceNumber(newData.insuranceNumber());
        patient.setInsuranceProvider(newData.insuranceProvider());
        patient.setAddress(newData.address());
        patient.setCreatedAt(LocalDateTime.now());
        patient.setDateOfBirth(newData.dateOfBirth());
        patient.setEmail(newData.email().toLowerCase().trim());
        patient.setGender(newData.gender());
        patient.setNationalId(newData.nationalId().trim());
        patient.setPhone(newData.phone().trim());
        patient.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Converts a patient object to a DTO
     * @param patient The patient object
     * @return The patient DTO
     */
    public static PatientDto toDto(Patient patient) {

        return new PatientDto(
                patient.getId(),
                patient.getFullName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getNationalId(),
                patient.getAddress(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getImage(),
                patient.getEmergencyContact(),
                patient.getEmergencyName(),
                patient.getInsuranceProvider(),
                patient.getInsuranceNumber(),
                patient.getBloodType(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
