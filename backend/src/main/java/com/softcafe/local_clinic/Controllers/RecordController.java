package com.softcafe.local_clinic.Controllers;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.Record.RecordList;
import com.softcafe.local_clinic.DTO.APIResponse.Record.RecordNotFound;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.Record.RecordResponse;
import com.softcafe.local_clinic.DTO.Record.NewRecordDTO;
import com.softcafe.local_clinic.Services.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Records controller", description = "Handles CRUD operations on the Record record")
public class RecordController {

    private final RecordService service;

    @Operation(summary = "Adds a record", description = "Adds a record to the system's database")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Record successfully added",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Record data missing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient/Doctor data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<APIInfoResponseDTO> addNewRecord(
            @RequestBody(
                    description = "The new record's data",
                    content = @Content(
                            schema = @Schema(implementation = NewRecordDTO.class)
                            )
            )
            @org.springframework.web.bind.annotation.RequestBody NewRecordDTO newRecord
    ) {
        return service.add(newRecord);
    }

    @Operation(summary = "Patient's records", description = "Retrieves a list of records belonging to a patient")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of records found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordList.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing patient id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient or records list not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
    })
    @GetMapping("/patient/{id}")
    public ResponseEntity<APIDataResponseDTO> getByPatient(
            @PathVariable Long id
    ) {
        return service.getByPatient(id);
    }

    @Operation(
            summary = "Get doctor's records",
            description = "Retrieves a list of records that were reviewed by a specific doctor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "The records list was found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordList.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "The doctor's details or the records' list wasn't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            )
    })
    @GetMapping("/doctor/{id}")
    public ResponseEntity<APIDataResponseDTO> getByDoctor(
            @Parameter(
                    description = "The doctor's primary key",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                    description = "The page number",
                    required = true,
                    example = "1"
            )
            @RequestParam(name = "pageNumber") int pageNumber
    ) {
        return service.getByDoctor(id, pageNumber);
    }

    @Operation(
            summary = "Record's by date",
            description = "Retrieves a list of records made between two specified dates"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "The list of records was found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordList.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid start and end date",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "No records found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            )
    })
    @GetMapping("/date")
    public ResponseEntity<APIDataResponseDTO> getByDatePeriod(
            @Parameter(
                    name = "startDate",
                    description = "The starting date",
                    required = true,
                    example = "2024-01-01"
            )
            @RequestParam(name = "startDate") LocalDate start,
            @Parameter(
                    name = "endDate",
                    description = "The ending date",
                    required = true,
                    example = "2024-12-31"
            )
            @RequestParam(name = "endDate") LocalDate end,
            @Parameter(
                    name = "pageNumber",
                    description = "The page number",
                    required = true,
                    example = "1"
            )
            @RequestParam(name = "pageNumber") int page
            ) {
        return service.getByDate(start, end, page);
    }

    @Operation(summary = "Retrieve a record", description = "Retrieves a records data by its primary key")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Record found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing patient id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RecordNotFound.class
                            )
                    )
            ),
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIDataResponseDTO> getById(
            @Parameter(
                    description = "The record's primary key",
                    required = true,
                    example = "2"
            )
            @PathVariable Long id
    ) {
        return service.get(id);
    }

    @Operation(
            summary = "Updates a record",
            description = "Updates an existing record's data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Record updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing id or update data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Record/Patient/Doctor data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> updateRecord(
            @Parameter(
                    description = "The record's primary key",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody(
                    description = "The updated data",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NewRecordDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody NewRecordDTO dto
    ) {
        return service.update(id, dto);
    }

    @Operation(
            summary = "Delete record",
            description = "Removes an existing record from the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Record successfully deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> deleteRecord(
            @Parameter(
                    description = "The record's primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return service.delete(id);
    }
}
