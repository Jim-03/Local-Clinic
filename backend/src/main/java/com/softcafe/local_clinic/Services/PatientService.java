package com.softcafe.local_clinic.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.softcafe.local_clinic.DTO.Patient.PatientDTO;
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
    public ResponseEntity<Map<String, String>> add(PatientDTO patient) {
        // Check if the patient's data is provided
        if (patient == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the patient's data!");
        }

        try {
            // Check if the patient data already exists
            if (patientExists(patient.email(), patient.phone(), patient.nationalId())) {
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
    private Patient getPatientData(PatientDTO patient) {
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
                patient.getInsuranceProvider(),
                patient.getInsuranceNumber(),
                patient.getKinName(),
                patient.getKinContact()
        );
    }

    /**
     * Retrieves a patient's data by their primary key
     * @param id The primary key
     * @return A Response Entity containing the patient's data or null in the mapped body
     */
    public ResponseEntity<Map<String, Object>> get(Long id) {
        // Check if ID is provided and valid
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid ID!", null);
        }

        try {
            // Fetch the patient's data
            Optional<User> patient = userRepository.findById(id);

            // Check if patient exists
            if (patient.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "Patient not found!", null);
            }

            // Return the patient's data
            return Responses.dataResponse(Status.SUCCESS, "Patient found", toDTO(patient));
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the patient's data: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Updates an existing patient's data
     * @param id The patient's primary key
     * @param updatedData The patient's new data
     * @return A Response Entity having a mapped object describing the result as the body
     */
    @Transactional
    public ResponseEntity<Map<String, String>> update(Long id, PatientDTO updatedData) {
        // Check if the argument is provided
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid ID!");
        }

        if (updatedData == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the updated data!");
        }

        try {
            // Fetch the patient's data
            Optional<Patient> patient = patientRepository.findById(id);

            // Check if patient exists
            if (patient == null || patient.isEmpty()) return Responses.infoResponse(Status.NOT_FOUND, "The patient wasn't found!");

            // Check if new data violates unique constraint
            if (patientExists(updatedData.email(), updatedData.phone(), updatedData.nationalId())) {
                return Responses.infoResponse(Status.DUPLICATE, "A patient with this data already exists!");
            }

            // Update the data
            Patient newData = getPatientData(updatedData);

            // Save the updated data
            patientRepository.save(newData);
            return Responses.infoResponse(Status.SUCCESS, "Patient successfully updated");
        } catch (Exception e) {
            System.err.println("An error has occurred while updating the patient: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    private boolean patientExists(String email, String phone, String nationalId) {
        // Check if the new data violates unique constraint
        Optional<User> exists = Optional.empty();

        // Fetch the user data
        if (email != null) {
            exists = userRepository.findByEmailAddress(email);
        }
        if (exists.isEmpty() && nationalId != null) {
            exists = userRepository.findByNationalId(nationalId);
        }
        if (exists.isEmpty() && phone != null) {
            exists = userRepository.findByPhoneNumber(phone);
        }

        return exists.isPresent();
    }

    /**
     * Removes a patient's data from the system
     * @param id The patient's primary key
     * @return A Response Entity containing a mapped object body confirming if the patient was deleted
     */
    @Transactional
    public ResponseEntity<Map<String, String>> delete(Long id) {
        // Check if id is provided and valid
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }

        try {
            // Fetch the patient's data
            Optional<Patient> patient = patientRepository.findById(id);

            // Check if patient exists
            if (patient == null || patient.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Patient wasn't found!");
            }

            // Delete the patient's data
            patientRepository.delete(patient.get());

            return Responses.infoResponse(Status.SUCCESS, "Patient successfully deleted!");
        } catch (Exception e) {
            System.err.println("An error has occurred while deleting the user: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }
}
