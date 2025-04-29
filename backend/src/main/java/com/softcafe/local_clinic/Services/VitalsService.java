package com.softcafe.local_clinic.Services;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.Vitals.PaginatedVitals;
import com.softcafe.local_clinic.DTO.Vitals.NewVitals;
import com.softcafe.local_clinic.Entities.Record;
import com.softcafe.local_clinic.Entities.Vitals;
import com.softcafe.local_clinic.Repositories.RecordRepository;
import com.softcafe.local_clinic.Repositories.VitalsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VitalsService {
    private final VitalsRepository vitalsRepository;
    private final RecordRepository recordRepository;

    /**
     * Adds a new vitals data to the database
     * @param newVitals The new data
     * @return A Response Entity containing an object confirming of the vitals were added
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> add(NewVitals newVitals) {
        // Check if vitals are provided
        if (newVitals == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the vitals data");
        }

        try {
            // Fetch the record's data
            Optional<Record> record = recordRepository.findById(newVitals.recordId());

            // Check if record exists
            if (record.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Record not found!");
            }

            // Add the vitals
            vitalsRepository.save(getVitalsData(newVitals, record.get()));
            return Responses.infoResponse(Status.CREATED, "Vitals successfully added");
        } catch (Exception e) {
            System.err.println("An error has occurred while saving the vitals: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Retrieves a vitals details by record id
     * @param id The record's primary key
     * @return A Response Entity containing an object having the vitals data or null
     */
    public ResponseEntity<APIDataResponseDTO> getByRecord(Long id) {
        // Validate the id
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid id!", null);
        }

        try {
            // Fetch the record's data
            Optional<Record> record = recordRepository.findById(id);

            // Check if record exists
            if (record.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The specified record doesn't exist!", null);
            }

            // Fetch the vitals
            Optional<Vitals> vitals = vitalsRepository.findByRecord(record);

            // Check if vitals exist
            if (vitals.isEmpty()) {
                return Responses.dataResponse(
                        Status.NOT_FOUND,
                        "The specified record doesn't have any vitals!",
                        null
                );
            }

            return Responses.dataResponse(Status.SUCCESS, "Vitals found", vitals.get());
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the vitals details" + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Retrieves a vitals details
     * @param id The vitals primary key
     * @return A Response Entity with an object containing the vitals details or null
     */
    public ResponseEntity<APIDataResponseDTO> getById(Long id) {
        // Validate the id
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid id!", null);
        }

        try {
            // Fetch the vitals
            Optional<Vitals> vitals = vitalsRepository.findById(id);

            // Check if vitals exist
            if (vitals.isEmpty()) {
                return Responses.dataResponse(
                        Status.NOT_FOUND,
                        "The specified vitals doesn't exist!",
                        null
                );
            }

            return Responses.dataResponse(Status.SUCCESS, "Vitals found", vitals.get());
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the vitals details" + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Updates an existing vitals data
     * @param id The vitals' primary key
     * @param newData The new vitals data
     * @return A Response Entity with an object confirming if a vitals data is updated
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> update(Long id, NewVitals newData) {
        // Validate the arguments
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }

        if (newData == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the updated data!");
        }

        try {
            // Fetch the old data
            Optional<Vitals> oldData = vitalsRepository.findById(id);

            // Check if the vitals exist
            if (oldData.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Vitals not found!");
            }

            // Update and save the new data
            vitalsRepository.save(updated(oldData.get(), newData));
            return Responses.infoResponse(Status.SUCCESS, "Vitals updated");
        } catch (Exception e) {
            System.err.println("An error has occurred while updating the vitals: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Deleted an existing vitals data
     * @param id The vitals primary key
     * @return A Response Entity containing an object that confirms if a vitals data is deleted
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> delete(Long id) {
        // Validate id
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }

        try {
            // Fetch the vitals data
            Optional<Vitals> vitals = vitalsRepository.findById(id);

            // Check if vitals exist
            if (vitals.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Vitals not found!");
            }

            vitalsRepository.delete(vitals.get());
            return Responses.infoResponse(Status.SUCCESS, "Vitals deleted");
        } catch (Exception e) {
            System.err.println("An error has occurred while deleting the vitals: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Updates a vitals object data
     * @param oldData The old data
     * @param dto The new data
     * @return An updated vitals object
     */
    private Vitals updated(Vitals oldData, NewVitals dto) {
        oldData.setBodyTemperature(dto.temperature());
        oldData.setDiastolic(dto.diastolicNumber());
        oldData.setHeartRate(dto.heartRate());
        oldData.setHeight(dto.height());
        oldData.setMass(dto.mass());
        oldData.setSystolic(dto.systolicNumber());
        return oldData;
    }

    /**
     * Creates a vitals object
     * @param dto The new data
     * @param record The record's data
     * @return A vitals object
     */
    private Vitals getVitalsData(NewVitals dto, Record record) {
        Vitals vitals = new Vitals();
        vitals.setBodyTemperature(dto.temperature());
        vitals.setDiastolic(dto.diastolicNumber());
        vitals.setHeartRate(dto.heartRate());
        vitals.setHeight(dto.height());
        vitals.setMass(dto.mass());
        vitals.setRecord(record);
        vitals.setSystolic(dto.systolicNumber());
        return vitals;
    }

    /**
     * Fetches a list of vitals records made between a specified range
     * @param date A dto containing the start/end date and the page
     * @return A Response Entity with a body containing the paginated list of records or null
     */
    public ResponseEntity<APIDataResponseDTO> getByDate(GetByDate date) {
        if (date.start() == null || date.end() == null) {
            return Responses.dataResponse(Status.REJECTED, "Provide valid date periods!", null);
        }

        try {
            Page<Vitals> vitalsPage;

            if (date.page() == 0) {
                vitalsPage = vitalsRepository.findByCreatedAtBetween(date.start(), date.end(), Pageable.unpaged());
            } else {
                vitalsPage = vitalsRepository.findByCreatedAtBetween(date.start(), date.end(), PageRequest.of(date.page() - 1, 10));
            }

            List<Vitals> vitalsList = vitalsPage.getContent();
            int totalPages = vitalsPage.getTotalPages();

            // Check if list is empty
            if (vitalsList.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "No vitals record made in the specified period!", new PaginatedVitals(vitalsList, totalPages));
            }

            return Responses.dataResponse(Status.SUCCESS, "Vitals found", vitalsList);
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the list of vitals created today: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error occurred!", null);
        }
    }
}
