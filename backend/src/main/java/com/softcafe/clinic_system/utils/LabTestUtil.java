package com.softcafe.clinic_system.utils;

import com.softcafe.clinic_system.dto.test.NewTest;
import com.softcafe.clinic_system.dto.test.TestData;
import com.softcafe.clinic_system.entities.LabTest;
import com.softcafe.clinic_system.entities.Record;

public class LabTestUtil {

    /**
     * Converts a DTO to a test object
     *
     * @param dto    Test DTO
     * @param record Record object
     * @return Test Object
     */
    public static LabTest toTest(NewTest dto, Record record) {
        LabTest test = new LabTest();
        test.setRecord(record);
        test.setInvestigations(dto.investigations());
        test.setFindings(dto.findings());

        return test;
    }

    /**
     * Converts a test object to DTO
     *
     * @param test Test data
     * @return Test DTO
     */
    public static TestData toDto(LabTest test) {
        return new TestData(
                test.getId(),
                RecordUtil.toDTO(test.getRecord()),
                test.getInvestigations(),
                test.getFindings(),
                test.getCreatedAt(),
                test.getUpdatedAt()
        );
    }

    /**
     * Updates a test object data
     *
     * @param record      Record object
     * @param test        Existing test object
     * @param updatedData Newly updated object
     */
    public static void update(Record record, LabTest test, NewTest updatedData) {
        test.setRecord(record);
        test.setFindings(updatedData.findings());
        test.setInvestigations(updatedData.investigations());
    }
}
