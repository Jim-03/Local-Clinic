package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.record.NewRecord;
import com.softcafe.clinic_system.dto.record.RecordData;
import com.softcafe.clinic_system.dto.record.RecordsList;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.Record;
import com.softcafe.clinic_system.entities.Staff;
import com.softcafe.clinic_system.repositories.PatientRepository;
import com.softcafe.clinic_system.repositories.RecordRepository;
import com.softcafe.clinic_system.repositories.StaffRepository;
import com.softcafe.clinic_system.utils.RecordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {
    private static final int PAGE_SIZE = 10;
    private final RecordRepository recordRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    /**
     * Adds a new record to the system
     *
     * @param newRecord THe new record details
     * @return The saved record's data
     * @throws ResponseStatusException BAD_REQUEST In case of invalid or missing data
     *                                 NOT_FOUND In case of missing patient/doctor record
     */
    @Transactional
    public RecordData add(NewRecord newRecord) {
        // Check if patient exists
        Patient patient = getPatient(newRecord.patientId());

        // Check if doctor exists
        Staff doctor = getDoctor(newRecord.doctorId());

        RecordData recordData = RecordUtil.toDTO(recordRepository.save(RecordUtil.toRecord(
                patient,
                doctor,
                newRecord
        )));

        recordRepository.flush();

        log.info("A new patient record with ID:{} has been saved", recordData.id());
        return recordData;
    }

    /**
     * Fetches records related to a patient
     *
     * @param id   The patient's primary key
     * @param page Page number
     * @return A record object containing the list of records and expected pages
     * @throws ResponseStatusException NOT_FOUND In case the patient's details weren't found
     */
    public RecordsList getByPatient(Long id, int page) {
        // Check if patient exists
        Patient patient = getPatient(id);

        // Fetch the list of records
        Page<Record> recordsPage = recordRepository.findByPatient(patient,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "created_at"))
        );

        List<RecordData> recordDataList = new ArrayList<>();

        for (Record record : recordsPage) {
            recordDataList.add(RecordUtil.toDTO(record));
        }

        return new RecordsList(recordsPage.getTotalPages(), recordDataList);
    }

    /**
     * Retrieves a list of records reviewed by a doctor
     *
     * @param id   Doctor's primary key
     * @param page Page number
     * @return A record object with the list of records and total expected pages
     * @throws ResponseStatusException NOT_FOUND In case doctor's details weren't found
     */
    public RecordsList getByDoctor(Long id, int page) {
        // Check if doctor exists
        Staff doctor = getDoctor(id);

        // Fetch the records list
        Page<Record> recordPage = recordRepository.findByDoctor(doctor, PageRequest.of(
                page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "created_at")
        ));

        List<RecordData> recordDataList = new ArrayList<>();

        for (Record record : recordPage) {
            recordDataList.add(RecordUtil.toDTO(record));
        }

        return new RecordsList(recordPage.getTotalPages(), recordDataList);
    }

    /**
     * Retrieves a list of records made between a given date range
     *
     * @param start Starting date
     * @param end   Ending date
     * @param page  Page number
     * @return A record object containing the list of records and expected total pages
     * @throws ResponseStatusException BAD_REQUEST In case the ending date is before starting date
     */
    public RecordsList getByDateRange(LocalDateTime start, LocalDateTime end, int page) {
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The end date should be after the starting date!");
        }

        Page<Record> recordPage = recordRepository.findByCreatedAtBetween(start, end, PageRequest.of(
                page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "created_at")
        ));

        List<RecordData> list = new ArrayList<>();

        for (Record record : recordPage) {
            list.add(RecordUtil.toDTO(record));
        }

        return new RecordsList(recordPage.getTotalPages(), list);
    }

    /**
     * Updates a record's data
     *
     * @param id            Primary key
     * @param updatedRecord The newly updated data
     * @return The saved updated data
     * @throws ResponseStatusException NOT_FOUND In case the record/doctor/patient record wasn't found
     */
    @Transactional
    public RecordData update(Long id, NewRecord updatedRecord) {
        // Fetch record's details
        Record record = recordRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified record doesn't exist!"));

        // Fetch the patient's details
        Patient patient = getPatient(updatedRecord.patientId());

        // Fetch doctor's details
        Staff doctor = getDoctor(updatedRecord.doctorId());

        // Update the record
        RecordUtil.update(record, updatedRecord, patient, doctor);

        log.info("Record with ID:{} was updated", id);

        return RecordUtil.toDTO(recordRepository.save(record));
    }

    /**
     * Removes a record from the system
     *
     * @param id Record's primary key
     * @throws ResponseStatusException In case the record wasn't found
     */
    @Transactional
    public void delete(Long id) {
        // Fetch the record's data
        Record record = recordRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified record wasn't found!"));

        recordRepository.delete(record);

        recordRepository.flush();

        log.info("Record with ID:{} was deleted", id);
    }

    /**
     * Retrieves the doctor's details
     *
     * @param id Doctor's primary key
     * @return Doctor's details
     * @throws ResponseStatusException NOT_FOUND In case the doctor's record wasn't found
     */
    private Staff getDoctor(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified doctor wasn't found!"));
    }

    /**
     * Retrieves the patient's details
     *
     * @param id Patient's primary key
     * @return Patient's details
     * @throws ResponseStatusException NOT_FOUND In case the patient's record wasn't found
     */
    private Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified patient wasn't found!"));
    }
}
