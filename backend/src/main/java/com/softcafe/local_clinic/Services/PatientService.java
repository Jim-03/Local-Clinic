package com.softcafe.local_clinic.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.softcafe.local_clinic.DTO.Patient.AddPatientDTO;
import com.softcafe.local_clinic.DTO.Patient.PatientDataDTO;
import com.softcafe.local_clinic.DTO.Patient.SearchPatientDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.softcafe.local_clinic.Entities.Patient;
import com.softcafe.local_clinic.Entities.User;
import com.softcafe.local_clinic.Repositories.PatientRepository;
import com.softcafe.local_clinic.Repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    /**
     * Adds a new user to the database
     * @param patient The new patient's data
     * @return A Response entity with its appropriate HTTP status code containing a map object as the body
     */
    @Transactional
    public ResponseEntity<Map<String, String>> add(AddPatientDTO patient) {
        // Check if the patient's data is provided
        if (patient == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the patient's data!");
        }

        try {
            // Check if the patient data already exists
            Optional<User> existsByPhone = userRepository.findByPhoneNumber(patient.phone());
            Optional<User> existsByEmail = userRepository.findByEmailAddress(patient.email());
            Optional<User> existsByNationalId = userRepository.findByNationalId(patient.nationalId());

            if (existsByEmail.isPresent() || existsByPhone.isPresent() || existsByNationalId.isPresent()) {
                return Responses.infoResponse(Status.DUPLICATE, "A patient with this data already exists!");
            }

            // Add the new patient's data
            patientRepository.save(getPatientData(patient));
            return Responses.infoResponse(Status.CREATED, "Patient successfully added");
        } catch (Exception e) {
            System.err.println("An error has occurred while adding the new patient: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred. Please try again!");
        }

    }

    /**
     * Creates a patient object from the patientDTO
     * @param patient The patient's
     * @return The patient object
     */
    private Patient getPatientData(AddPatientDTO patient) {
        // Create the patient object
        Patient newPatient = new Patient();

        // Map the data to the object
        newPatient.setFullName(patient.fullName());
        newPatient.setDateOfBirth(LocalDate.parse(patient.dob()));
        newPatient.setEmailAddress(patient.phone());
        newPatient.setPhoneNumber(patient.email());
        newPatient.setAddress(patient.address());
        newPatient.setCreatedAt(LocalDateTime.now());
        newPatient.setUpdatedAt(LocalDateTime.now());

        newPatient.setInsuranceProvider(patient.insuranceProvider());
        newPatient.setInsuranceNumber(patient.insuranceNumber());
        newPatient.setKinName(patient.kinName());
        newPatient.setKinContact(patient.kinContact());
        return newPatient;
    }

    /**
     * Retrieves a patient's data
     * @param patientDTO A DTO containing the patient's unique identifiers:
     *                   email: String,
     *                   phone: String,
     *                   cardNumber: String
     * @return A Response Entity containing a mapped object as its body
     */
    public ResponseEntity<Map<String, Object>> find(SearchPatientDTO patientDTO) {
        // Check if any data was provided
        if (patientDTO == null) {
            return Responses.dataResponse(Status.REJECTED, "Provide one identifier", null);
        }

        try {
            // Create an empty patient object
            Optional<User> patient = Optional.empty();

            // Fetch the user data
            if (patientDTO.email() != null) {
                patient = userRepository.findByEmailAddress(patientDTO.email());
            }
            if (patient.isEmpty() && patientDTO.cardNumber() != null) {
                patient = userRepository.findByNationalId(patientDTO.cardNumber());
            }
            if (patient.isEmpty() && patientDTO.phone() != null) {
                patient = userRepository.findByPhoneNumber(patientDTO.phone());
            }

            if (patient.isEmpty()) return Responses.dataResponse(Status.NOT_FOUND, "Patient not found!", null);


            // Return the patient data
            return Responses.dataResponse(Status.SUCCESS, "Patient found", toDTO(patient));
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the patient's data: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Converts the patient object to its data transfer object
     * @param data The patient's data
     * @return A patient's DTO
     */
    private PatientDataDTO toDTO(Optional<User> data) {
        // Cast to patient
        Patient patient = (Patient) data.get();

        return new PatientDataDTO(
                patient.getId(),
                patient.getFullName(),
                patient.getPhoneNumber(),
                patient.getEmailAddress(),
                patient.getNationalId(),
                patient.getDateOfBirth(),
                patient.getAddress(),
                patient.getBloodType(),
                patient.getAllergies(),
                patient.getExistingConditions(),
                patient.getInsuranceProvider(),
                patient.getInsuranceNumber(),
                patient.getKinName(),
                patient.getKinContact()
        );
    }
}
