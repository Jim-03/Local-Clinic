package com.softcafe.clinic_system.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.softcafe.clinic_system.dto.test.NewTest;
import com.softcafe.clinic_system.dto.test.TestData;
import com.softcafe.clinic_system.dto.test.TestList;
import com.softcafe.clinic_system.entities.LabTest;
import com.softcafe.clinic_system.entities.Record;
import com.softcafe.clinic_system.repositories.LabTestRepository;
import com.softcafe.clinic_system.repositories.RecordRepository;
import com.softcafe.clinic_system.utils.LabTestUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class LabTestService {

    private final RecordRepository recordRepository;
    private final LabTestRepository labTestRepository;

    /**
     * Adds a new test to the system
     *
     * @param newTest New test data
     * @return Newly saved test data
     */
    @Transactional
    public TestData add(NewTest newTest) {
        // Fetch record's data
        Record record = getRecord(newTest.recordId());

        TestData save = LabTestUtil.toDto(labTestRepository.save(LabTestUtil.toTest(newTest, record)));
        labTestRepository.flush();

        log.info("A new test with ID:{} was added", save.id());

        return save;
    }

    /**
     * Retrieves a list of tests associated with a record
     *
     * @param id Record's primary key
     * @return List of tests
     */
    public List<TestData> getByRecord(Long id) {
        // Fetch record's details
        Record record = getRecord(id);

        List<TestData> tests = new ArrayList<>();

        for (LabTest test : labTestRepository.findAllByRecord(record)) {
            tests.add(LabTestUtil.toDto(test));
        }

        return tests;
    }

    /**
     * Retrieves a list of tests created between a given date range
     *
     * @param start Starting date
     * @param end Ending date
     * @param pageNumber Page number
     * @return An object containing the total pages and the list of tests
     */
    public TestList getByDateRange(LocalDateTime start, LocalDateTime end, int pageNumber) {
        // Validate the dates
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid date range");
        }

        Page<LabTest> page = labTestRepository.findByCreatedAtBetween(
                start, end, PageRequest.of(pageNumber - 1, 10, Sort.by(
                        Sort.Direction.DESC, "created_at"
                ))
        );

        List<TestData> list = new ArrayList<>();

        for (LabTest test : page) {
            list.add(LabTestUtil.toDto(test));
        }

        return new TestList(page.getTotalPages(), list);
    }

    /**
     * Updates an existing test's data
     *
     * @param id Test's primary key
     * @param updatedData Newly updated object data
     * @return Newly saved test data
     * @throws ResponseStatusException NOT_FOUND In case the specified
     * test/record wasn't found
     */
    public TestData update(Long id, NewTest updatedData) {
        // Fetch the test data
        LabTest test = getTest(id);

        // Fetch the record
        Record record = getRecord(updatedData.recordId());

        LabTestUtil.update(record, test, updatedData);

        labTestRepository.save(test);
        labTestRepository.flush();

        log.info("Lab test with ID:{} was updated", id);
        return LabTestUtil.toDto(test);
    }

    /**
     * Removes a lab test from the system
     *
     * @param id Lab Test primary key
     */
    public void delete(Long id) {
        // Fetch the test's data
        LabTest test = getTest(id);

        labTestRepository.delete(test);

        log.info("Lab Test with ID:{} was deleted", id);
    }

    /**
     * Retrieves a record's details
     *
     * @param id Record's primary key
     * @return Record data
     */
    private Record getRecord(Long id) {
        return recordRepository.findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified record doesn't exist!")
                );
    }

    /**
     * Retrieves a test's data by its id
     *
     * @param id Primary key
     * @return The test's data
     * @throws ResponseStatusException NOT_FOUND In case the test wasn't found
     */
    private LabTest getTest(Long id) {
        return labTestRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified test doesn't exist!")
        );
    }
}
