package com.softcafe.local_clinic.Services;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.Record.NewRecordDTO;
import com.softcafe.local_clinic.Entities.Patient;
import com.softcafe.local_clinic.Entities.Record;
import com.softcafe.local_clinic.Entities.Staff;
import com.softcafe.local_clinic.Repositories.PatientRepository;
import com.softcafe.local_clinic.Repositories.RecordRepository;
import com.softcafe.local_clinic.Repositories.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final RecordRepository recordRepository;

    /**
     * Adds a new record to the system
     * @param newRecord The new record's data
     * @return A Response Entity with a body containing an object that describe if the record was successfully added
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> add(NewRecordDTO newRecord) {
        // Check if the new data is provided
        if (newRecord == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the new record's data!");
        }

        try {
            // Fetch the personnel details
            Optional<Patient> patient = patientRepository.findById(newRecord.patientId());
            Optional<Staff> doctor = staffRepository.findById(newRecord.staffId());

            // Check if they exist
            if (patient.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "The specified patient wasn't found!");
            }
            if (doctor.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "The specified doctor wasn't found!");
            }

            // Add the new record
            recordRepository.save(getRecordData(newRecord, patient.get(), doctor.get()));
            return Responses.infoResponse(Status.CREATED, "Record successfully added");
        } catch (Exception e) {
            System.err.println("An error has occurred while adding the record: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    public ResponseEntity<APIDataResponseDTO> get(Long id) {
        // Validate the id
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid ID!", null);
        }

        try {
            // Fetch the record's data
            Optional<Record> record = recordRepository.findById(id);

            // Check if record exists
            if (record.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "Record not found!", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Record found", record.get());
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the record: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Retrieves a list of records belonging to a patient
     * @param id The patient's primary key
     * @return A Response Entity containing the list of records or null
     */
    public ResponseEntity<APIDataResponseDTO> getByPatient(Long id) {
        // Validate the id
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide the patient's id!", null);
        }

        try {
            // Fetch the patient's data
            Optional<Patient> patient = patientRepository.findById(id);

            // Check if patient exists
            if (patient.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The patient wasn't found!", null);
            }

            // Fetch the records list
            List<Record> records = recordRepository.findByPatient(patient);

            // Check if patient has any records
            if (records.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The patient doesn't have any records!", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Records' list found", records);
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the records list: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Retrieves a list of records made by a doctor
     * @param id The doctor's primary key
     * @param page The page number
     * @return A Response Entity containing the list of records or null
     */
    public ResponseEntity<APIDataResponseDTO> getByDoctor(Long id, int page) {
        // Validate input
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid id", null);
        }

        try {
            // Fetch the doctor's details
            Optional<Staff> doctor = staffRepository.findById(id);

            // Check if doctor exists
            if (doctor.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The doctor wasn't found!", null);
            }

            // Fetch the records list
            List<Record> records = recordRepository.findByDoctor(doctor.get(), PageRequest.of(page, 10));

            // Check if records exist
            if (records.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The doctor hasn't reviewed any patient!", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Records found", records);
         } catch (Exception e) {
            System.err.println("An error has occurred while fetching the records: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Retrieves a list of record's between two specified periods
     * @param start The starting date
     * @param end The ending date
     * @param pageNumber The page number
     * @return A Response Entity  containing the list of records
     */
    public ResponseEntity<APIDataResponseDTO> getByDate(LocalDate start, LocalDate end, int pageNumber) {
        // Validate the inputs
        if (start == null || end == null) {
            return Responses.dataResponse(Status.REJECTED, "Provide valid date periods!", null);
        }

        try {
            // Fetch the records list
            List<Record> records = recordRepository.findByCreatedAtBetween(
                    start.atStartOfDay(),
                    end.plusDays(1).atStartOfDay(),
                    PageRequest.of(pageNumber, 10)
            );

            // Check if records exist
            if (records.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The records' list wasn't found!", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Records found", records);
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the records' list: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Updates an existing record's data
     * @param id The record's primary key
     * @param dto The newly updated data
     * @return A Response Entity containing an object confirming if the record was updated
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> update(Long id, NewRecordDTO dto) {
        // Validate arguments
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }

        if (dto == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the updated data!");
        }

        try {
            // Fetch the old data
            Optional<Record> oldData = recordRepository.findById(id);

            // Check if the record exists
            if (oldData.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "The specified record doesn't exist");
            }

            // Check if the new personnel details exist
            Optional<Staff> staff = Optional.empty();
            Optional<Patient> patient = Optional.empty();

            if (dto.staffId() != null) {
                staff = staffRepository.findById(dto.staffId());

                if (staff.isEmpty()) {
                    return Responses.infoResponse(Status.NOT_FOUND, "The specified doctor doesn't exist!");
                }
            }

            if (dto.patientId() != null) {
                patient = patientRepository.findById(dto.patientId());
                if (patient.isEmpty()) {
                    return Responses.infoResponse(Status.NOT_FOUND, "The specified patient doesn't exist!");
                }
            }

            // Update the record
            recordRepository.save(updatedRecord(oldData.get(), staff.get(), patient.get(), dto));
            return Responses.infoResponse(Status.SUCCESS, "Record updated");
        } catch (Exception e) {
            System.err.println("An error has occurred while updating the record: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Deleted an existing record's data
     * @param id The record's primary key
     * @return A Response Entity containing an object that confirms if the record was deleted
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> delete(Long id) {
        // Validate id
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }

        try {
            // Fetch the record's details
            Optional<Record> record = recordRepository.findById(id);

            // Check if record exists
            if (record.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Record wasn't found!");
            }

            recordRepository.delete(record.get());
            return Responses.infoResponse(Status.SUCCESS, "Record successfully deleted!");
        } catch (Exception e) {
            System.err.println("An error has occurred while deleting the record: " + e.getMessage());
            return  Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Builds a record object
     * @param dto The new record's data
     * @param patient The patient's data
     * @param doctor The doctor's data
     * @return A record object
     */
    private Record getRecordData(NewRecordDTO dto, Patient patient, Staff doctor) {
        Record record = new Record();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setCreatedAt(LocalDateTime.now());
        record.setDiagnosis(dto.diagnosis());
        record.setMedication(dto.medication());
        record.setNotes(dto.notes());
        record.setPhysicalSymptoms(dto.physicalSymptoms());
        record.setReason(dto.reason());
        record.setSymptoms(dto.symptoms());
        return record;
    }

    /**
     * Updates a record object's data
     * @param oldData The old data
     * @param doctor The doctor's details
     * @param patient The patient's details
     * @param dto The new data
     * @return An updated record object
     */
    private Record updatedRecord( Record oldData,Staff doctor, Patient patient, NewRecordDTO dto) {
        oldData.setPatient(patient);
        oldData.setDoctor(doctor);
        oldData.setDiagnosis(dto.diagnosis());
        oldData.setMedication(dto.medication());
        oldData.setNotes(dto.notes());
        oldData.setPhysicalSymptoms(dto.physicalSymptoms());
        oldData.setReason(dto.reason());
        oldData.setSymptoms(dto.symptoms());
        return oldData;
    }
}
