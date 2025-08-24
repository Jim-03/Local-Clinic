package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.patient.ListOfPatients;
import com.softcafe.clinic_system.dto.patient.NewPatient;
import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.repositories.PatientRepository;
import com.softcafe.clinic_system.utils.PatientUtil;
import com.softcafe.clinic_system.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private final int PAGE_SIZE = 10;

    /**
     * Retrieves a list of patients
     * @param pageNumber The page to extract the list
     * @return An object containing a list of patients' data nad total number of expected pages
     */
    public ListOfPatients getByPage(int pageNumber) {
        Page<Patient> page = patientRepository.findAll(PageRequest.of(pageNumber - 1, PAGE_SIZE));
        List<Patient> patients= page.toList();
        List<PatientDto> list = new ArrayList<>();
        // Convert each patient data to DTO
        for(Patient patient: patients) {
            list.add(PatientUtil.toDto(patient));
        }

        return new ListOfPatients(page.getTotalPages(), list);
    }

    /**
     * Retrieves the total number of patients in the system
     * @return The total number
     */
    public long getTotalNumberOfPatients() {
        return patientRepository.count();
    }

    /**
     * Obtains the total number of pages that can be queried
     * @return Total number of pages
     */
    public int getTotalPages() {
        return patientRepository.findAll(PageRequest.ofSize(PAGE_SIZE)).getTotalPages();
    }

    /**
     * Adds a new patient to the system
     * @param dto The new patient's data
     * @return The newly created data
     * @throws ResponseStatusException BAD_REQUEST if patient data is missing
     * @throws ResponseStatusException CONFLICT if patient data is a duplicate of another record
     */
    @Transactional
    public PatientDto addPatient(NewPatient dto) {
        // Check if the new data is provided
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing new patient data!");
        }

        try {
            // Validate the data
            PatientUtil.validate(dto);
            Patient patient = patientRepository.save(PatientUtil.toPatient(dto));
            patientRepository.flush();
            log.info("A new patient with ID: {} has been added", patient.getId());
            return PatientUtil.toDto(patient);
        } catch (DataIntegrityViolationException e) { // Handle errors related to constraints (unique constraint)
            String violatedField = Util.parseViolation(e);
            String message = violatedField == null ?
                    "A patient with these details already exists!" :
                    "A patient with this " + violatedField + " already exists!";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        } catch (IllegalArgumentException e) { // Handle missing/incorrect parameters
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Updates an existing patient's data
     * @param id The patient's primary key
     * @param newData The patient's new data
     * @return The newly updated data
     * @throws ResponseStatusException BAD_REQUEST In case of missing data
     * @throws ResponseStatusException NOT_FOUND In case the patient's data wasn't found
     * @throws ResponseStatusException CONFLICT If data violates unique constraint
     */
    @Transactional
    public PatientDto update(Long id, NewPatient newData) {
        Util.validateId(id);
        if (newData == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the updated data!");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        try {
            PatientUtil.validate(newData);
            PatientUtil.updatePatient(patient, newData);
            log.info("Patient with ID: {} was updated", patient.getId());
            return PatientUtil.toDto(patientRepository.save(patient));
        } catch (DataIntegrityViolationException e) {
            String violatedField = Util.parseViolation(e);
            String message = violatedField == null ?
                    "A patient with these details already exists!" :
                    "A patient with this " + violatedField + " already exists!";
            log.warn(message);
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Removes an existing patient's data from the system
     * @param id The patient's primary key
     * @throws ResponseStatusException BAD_REQUEST In case of missing ID
     * @throws ResponseStatusException NOT_FOUND In case the patient's data wasn't found
     */
    @Transactional
    public void remove(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid ID!");
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        patientRepository.delete(patient);
        log.info("Patient with ID: {} was deleted", id);
    }

    /**
     * Fetches a patient's data
     * @param email An optional email address
     * @param phone An optional phone number
     * @param nid An optional nation ID card number
     * @return The patient's data or null
     * @throws ResponseStatusException BAD_REQUEST in case of null identifiers
     * @throws ResponseStatusException NOT_FOUND in case of missing patient data
     */
    public PatientDto get(String email, String phone, String nid) {
        // Check if all parameters are null
        if (email == null && phone == null && nid == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide at least one identifier!");
        }

            PatientDto dto = null;

            if (email != null) {
                if (Util.isValidEmail(email)) {
                    dto = PatientUtil.toDto(patientRepository.findByEmail(email).get());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid email address!");
                }
            } else if (dto == null && phone != null) {
                if (Util.isValidPhone(phone)) {
                    dto = PatientUtil.toDto(patientRepository.findByPhone(phone).get());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid phone number!");
                }
            } else if (dto == null && nid != null) {
                dto = PatientUtil.toDto(patientRepository.findByNationalId(nid).get());
            } else {
                dto = PatientUtil.toDto(patientRepository.findByInsuranceNumber(inn).get());
            }

            if (dto == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified patient wasn't found!");
            }

            return dto;
    }
}