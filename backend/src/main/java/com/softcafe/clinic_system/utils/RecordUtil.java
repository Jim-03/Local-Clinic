package com.softcafe.clinic_system.utils;

import com.softcafe.clinic_system.dto.record.NewRecord;
import com.softcafe.clinic_system.dto.record.RecordData;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.Record;
import com.softcafe.clinic_system.entities.Staff;

public class RecordUtil {

    /**
     * Converts a DTO to a record object
     *
     * @param patient   Patient's data
     * @param staff     Doctor's data
     * @param newRecord Record DTO
     * @return Record object
     */
    public static Record toRecord(Patient patient, Staff staff, NewRecord newRecord) {
        Record record = new Record();
        record.setDoctor(staff);
        record.setPatient(patient);
        record.setDiagnosis(newRecord.diagnosis());
        record.setNotes(newRecord.notes());
        record.setReason(newRecord.reason());
        record.setSymptoms(newRecord.symptoms());
        record.setTreatment(newRecord.treatment());
        return record;
    }

    /**
     * Converts record object to a DTO
     *
     * @param record Record object
     * @return Record DTO
     */
    public static RecordData toDTO(Record record) {
        return new RecordData(
                record.getId(),
                PatientUtil.toDto(record.getPatient()),
                StaffUtil.toDto(record.getDoctor()),
                record.getReason(),
                record.getSymptoms(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getNotes(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }

    /**
     * Updates a record object from a DTO
     *
     * @param record        Record object
     * @param updatedRecord Record DTO
     * @param patient       Patient's details
     * @param doctor        Doctor's details
     */
    public static void update(Record record, NewRecord updatedRecord, Patient patient, Staff doctor) {
        record.setDoctor(doctor);
        record.setPatient(patient);
        record.setDiagnosis(updatedRecord.diagnosis());
        record.setNotes(updatedRecord.notes());
        record.setReason(updatedRecord.reason());
        record.setSymptoms(updatedRecord.symptoms());
        record.setTreatment(updatedRecord.treatment());
    }
}
