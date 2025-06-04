package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.record.NewRecord;
import com.softcafe.clinic_system.dto.record.RecordData;
import com.softcafe.clinic_system.dto.record.RecordsList;
import com.softcafe.clinic_system.services.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @Operation(summary = "Get patient's records")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Records found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecordsList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid page/id details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"ID should at least 1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's details weren't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient doesn't exist!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @GetMapping("/patient/{id}")
    public ResponseEntity<RecordsList> getByPatient(
            @NotNull
            @Min(value = 1, message = "ID should be at least 1")
            @Parameter(
                    description = "The patient's ID",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @NotNull
            @Min(value = 1, message = "Pages start from 1")
            @Parameter(
                    description = "Page Number",
                    example = "1",
                    required = true
            )
            @PathParam("page") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(recordService.getByPatient(id, page));
    }

    @Operation(summary = "Get records reviewed by a doctor")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Records found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecordsList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid page/id details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"ID should at least 1\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Doctor's details weren't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified doctor doesn't exist!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )

    })
    @GetMapping("/doctor/{id}")
    public ResponseEntity<RecordsList> getByDoctor(
            @Parameter(description = "Doctor's primary key", example = "1", required = true)
            @PathVariable @NotNull @Min(value = 1, message = "ID should be at least 1") Long id,
            @Parameter(description = "Page number", example = "1", required = true)
            @PathParam("page") @NotNull @Min(value = 1, message = "Pages start from 1") int page
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(recordService.getByDoctor(id, page));
    }

    @Operation(summary = "Get records created in a date range")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Records found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecordsList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid page/date details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Ending date shouldn't be before starting date!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )

    })
    @GetMapping("/date")
    public ResponseEntity<RecordsList> getByDateRange(
            @PathParam("start") @NotNull(message = "Provide the starting date!") LocalDateTime start,
            @PathParam("end") @NotNull(message = "Provide the ending date!") LocalDateTime end,
            @PathParam("page") @NotNull(message = "Provide the page number!")
            @Min(value = 1, message = "Pages start from 1!") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(recordService.getByDateRange(start, end, page));
    }

    @Operation(summary = "Add a new record")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Record data added",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecordData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid record's details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"ID should be at least 1!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's/Doctor's details weren't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Patient's record not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )

    })
    @PostMapping
    public ResponseEntity<RecordData> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The new record's data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewRecord.class))
            )
            @RequestBody @NotNull(message = "Provide the new record's data!") NewRecord newRecord
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recordService.add(newRecord));
    }

    @Operation(summary = "Update a record")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Record data updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecordData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid record's details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"ID should be at least 1!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's/Doctor's/Record's details weren't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Patient's record not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )

    })
    @PutMapping("/{id}")
    public ResponseEntity<RecordData> update(
            @Parameter(description = "Record's primary key", example = "1", required = true)
            @PathVariable @NotNull(message = "Provide the record's primary key!")
            @Min(value = 1, message = "ID should be at least 1!") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The newly updated record data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewRecord.class))
            )
            @RequestBody NewRecord updatedRecord
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(recordService.update(id, updatedRecord));
    }

    @Operation(summary = "Delete a record")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Record data deleted"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid record ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"ID should be at least 1!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Record not found!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Record not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )

    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @Parameter(description = "Record's primary key", example = "1", required = true)
            @PathVariable @NotNull(message = "Provide the record's ID!")
            @Min(value = 1, message = "ID should be at least 1!") Long id
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
