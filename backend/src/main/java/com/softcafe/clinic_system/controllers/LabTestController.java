package com.softcafe.clinic_system.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softcafe.clinic_system.dto.test.NewTest;
import com.softcafe.clinic_system.dto.test.TestData;
import com.softcafe.clinic_system.dto.test.TestList;
import com.softcafe.clinic_system.services.LabTestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Tag(name = "Laboratory test controller", description = "Endpoints handling test data")
public class LabTestController {

    private final LabTestService testService;

    @Operation(summary = "Get by record", description = "Retrieves a list of tests done in a single visiting record")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200", description = "Tests found",
                content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(
                                schema = @Schema(implementation = TestData.class)
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400", description = "Missing/Invalid request data",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"Provide the Record's ID!\"}")
                )
        ),
        @ApiResponse(
                responseCode = "404", description = "Record not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"The specified record doesn't exist!\"}")
                )
        ),
        @ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"An error occurred\"}")
                )
        )

    })
    @GetMapping("/record/{id}")
    public ResponseEntity<List<TestData>> getByRecord(
            @Parameter(description = "Record's primary key", example = "1", required = true)
            @NotNull(message = "Provide the record's ID!") @Min(value = 1, message = "ID should be at least 1!")
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getByRecord(id));
    }

    @Operation(summary = "Get tests between two dates")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200", description = "List of tests found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TestList.class)
                )
        ),
        @ApiResponse(
                responseCode = "400", description = "Missing/Invalid request data",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"Provide the starting date\"}")
                )
        ),
        @ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"An error occurred\"}")
                )
        )
    })
    @GetMapping
    public ResponseEntity<TestList> getByDate(
            @Parameter(description = "The starting date",
                    schema = @Schema(example = "2025-12-12T00:00:00"), required = true)
            @RequestParam("start") @NotNull(message = "Provide the starting date!") LocalDateTime start,
            @Parameter(description = "The ending date",
                    schema = @Schema(example = "2025-12-12T23:59:59"), required = true)
            @RequestParam("end") @NotNull(message = "Provide the ending date!") LocalDateTime end,
            @Parameter(description = "Page number", example = "1", required = true)
            @RequestParam("page") @Min(value = 1, message = "Pages start from 1!") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getByDateRange(start, end, page));
    }

    @Operation(summary = "Update a test")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200", description = "Test updated",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TestData.class)
                )
        ),
        @ApiResponse(
                responseCode = "400", description = "Missing/Invalid request data",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"")
                )
        ),
        @ApiResponse(
                responseCode = "404", description = "Test/Record not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"The specified record doesn't exist!\"")
                )
        ),
        @ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"An error has occurred!\"")
                )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TestData> updateTest(
            @Parameter(description = "Test's primary key", example = "1", required = true)
            @NotNull(message = "Provide the test's ID!") @Min(value = 1, message = "ID should be at least 1!")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated test's data",
                    content = @Content(schema = @Schema(implementation = NewTest.class)),
                    required = true
            )
            @RequestBody NewTest updatedData
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.update(id, updatedData));
    }

    @Operation(summary = "Add a test")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201", description = "Test added",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = TestData.class)
                )
        ),
        @ApiResponse(
                responseCode = "400", description = "Missing/Invalid request data",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"")
                )
        ),
        @ApiResponse(
                responseCode = "404", description = "Record not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"The specified record doesn't exist!\"")
                )
        ),
        @ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"An error has occurred!\"")
                )
        )
    })
    @PostMapping
    public ResponseEntity<TestData> addNewTest(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New test data",
                    content = @Content(schema = @Schema(implementation = NewTest.class)),
                    required = true
            )
            @RequestBody NewTest newTest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.add(newTest));
    }

    @Operation(summary = "Removes a test")
    @ApiResponses({
        @ApiResponse(
                responseCode = "204", description = "Test deleted"
        ),
        @ApiResponse(
                responseCode = "400", description = "Missing/Invalid id",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"")
                )
        ),
        @ApiResponse(
                responseCode = "404", description = "Test not found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"The specified test doesn't exist!\"")
                )
        ),
        @ApiResponse(
                responseCode = "500", description = "Internal server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(example = "{\"message\": \"An error has occurred!\"")
                )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTest(
            @Parameter(description = "Test's primary key", example = "1", required = true)
            @NotNull(message = "Provide the test's ID!") @Min(value = 1, message = "ID should be at least 1!")
            @PathVariable Long id
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
