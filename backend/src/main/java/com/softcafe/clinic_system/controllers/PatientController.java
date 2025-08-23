package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.patient.ListOfPatients;
import com.softcafe.clinic_system.dto.patient.NewPatient;
import com.softcafe.clinic_system.dto.patient.PatientDto;
import com.softcafe.clinic_system.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@Tag(name = "Patient Controller", description = "Endpoints to the patient model")
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Adds a new patient")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Patient added successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the patient's full name\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Duplicate data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"A patient with this email exists\"}")
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
    public ResponseEntity<PatientDto> addPatient(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The new patient's data",
                    content = @Content(schema = @Schema(implementation = NewPatient.class)),
                    required = true
            ) NewPatient newPatient) {
            return ResponseEntity.status(HttpStatus.CREATED).body(patientService.addPatient(newPatient));
    }

    @Operation(summary = "Updates a patient's data")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Patient successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing or invalid id/updated data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the patient's full name\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Patient's data already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"A patient with this phone number exists\"}")
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
    public ResponseEntity<PatientDto> updatePatient(
            @Parameter(
                    description = "The patient's primary key",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The patient's new data",
                    content = @Content(
                            schema = @Schema(implementation = NewPatient.class)
                    )
            )
            @RequestBody NewPatient updated
    ) {
        return ResponseEntity.status(200).body(patientService.update(id, updated));
    }

    @Operation(summary = "Fetch patients by page", description = "Fetches a list of 10 patients in a specified page")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of patient's found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ListOfPatients.class),
                                    arraySchema = @Schema(description = "A list of 10 or less patient objects")
                            )
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
    @GetMapping("/page/{page}")
    public ResponseEntity<ListOfPatients> getByPage(
            @Parameter(
                    description = "The page number",
                    required = true,
                    example = "1"
            )
            @PathVariable int page
    ) {
        return ResponseEntity.status(200).body(patientService.getByPage(page));
    }

    @Operation(summary = "Fetch patient data", description = "Fetches a patient's data provided at least one identifier")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Patient data found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "No identifier provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide at least one identifier\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PatientDto> getById(
            @Parameter(
                    description = "Email address",
                    example = "john.doe@example.com"
            )
            @RequestParam("email") String email,
            @Parameter(
                    description = "Phone number",
                    example = "+254712345678"
            )
            @RequestParam("phone") String phone,
            @Parameter(
                    description = "National ID number",
                    example = "316353461"
            )
            @RequestParam("nid") String nid,
            @Parameter(
                    description = "Insurance number",
                    example = "SHA123"
            )
            @RequestParam("inn") String inn
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.get(email, phone, nid, inn));
    }

    @Operation(summary = "Deletes a patient")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Patient successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Patient not found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        patientService.remove(id);
        return ResponseEntity.status(204).build();
    }
}
