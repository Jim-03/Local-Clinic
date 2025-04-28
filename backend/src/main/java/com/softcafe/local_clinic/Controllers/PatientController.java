package com.softcafe.local_clinic.Controllers;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.Patient.PatientListFound;
import com.softcafe.local_clinic.DTO.APIResponse.Patient.PatientListNotFound;
import com.softcafe.local_clinic.DTO.Patient.AllPatients;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softcafe.local_clinic.DTO.Patient.PatientDTO;
import com.softcafe.local_clinic.DTO.Patient.SearchPatientDTO;
import com.softcafe.local_clinic.Services.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/patient/")
@RequiredArgsConstructor
@Tag(name = "Patient Controller", description = "Handles the requests/responses that deal with the patient model")
public class PatientController {

    private final PatientService service;

    @PostMapping
    @Operation(
            summary = "Adds a patient to the system",
            description = "Receives the patient's data from the frontend and passes it to the service class to be added to the database"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Patient successfully added",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIInfoResponseDTO.class))),

        @ApiResponse(responseCode = "400", description = "Invalid input or missing fields",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIInfoResponseDTO.class))),

        @ApiResponse(responseCode = "409", description = "Duplicate patient data",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIInfoResponseDTO.class))),

        @ApiResponse(responseCode = "500", description = "Server error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIInfoResponseDTO.class)))
    })
    public ResponseEntity<APIInfoResponseDTO> addPatient(
            @RequestBody(
                    description = "The new patients data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody PatientDTO patientDTO
    ) {
        return service.add(patientDTO);
    }

    @Operation(
            description = "Receives a patient's unique identifier to search for a patient in the system",
            summary = "Searches for a patient"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200", description = "Patient found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIDataResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "404", description = "Patient not found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIDataResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "400", description = "Missing identifiers",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIDataResponseDTO.class))
        ),
        @ApiResponse(
                responseCode = "500", description = "Server error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = APIDataResponseDTO.class))
        )
    })
    @PostMapping("/search")
    public ResponseEntity<APIDataResponseDTO> getPatient(
            @RequestBody(
                    description = "The patient's unique identifiers",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SearchPatientDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody SearchPatientDTO patientDTO
    ) {
        return service.find(patientDTO);
    }

    @Operation(description = "Gets the patient's data by the ID provided by the database", summary = "Retrieves patient's data")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Patient data found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = APIDataResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Id not provided or value is less or equal to 0",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = APIDataResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient not found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = APIDataResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = APIDataResponseDTO.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIDataResponseDTO> getPatient(
            @Parameter(
                    name = "id",
                    description = "The ID of the patient stored in the database",
                    required = true,
                    example = "12"
            )
            @PathVariable Long id) {
        return service.get(id);
    }

    @Operation(summary = "Get all patients", description = "Fetches a list of all patients via pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All patients found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PatientListFound.class))),
            @ApiResponse(
                    responseCode = "400", description = "Incorrect page number",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PatientListNotFound.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patients' list not found",
                    content = @Content(mediaType = "application/json",
                    schema =  @Schema(implementation = PatientListNotFound.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PatientListNotFound.class))
            )
    })
    @PostMapping("/all")
    public ResponseEntity<APIDataResponseDTO> getAll(
            @org.springframework.web.bind.annotation.RequestBody AllPatients allPatients
            ) {
        return service.getAll(allPatients);
    }

    @Operation(
            summary = "Updates a patient",
            description = "Searches for a patient with the provided database id," +
                    "it then updates the data with the provided request body"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Patient updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing id or updated data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> updatePatient(
            @RequestBody(
                    description = "The updated patient data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))
            )
            @Parameter(
                    name = "id",
                    description = "The patient's database ID",
                    example = "1"
            )
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody PatientDTO updatedData
    ) {
        return service.update(id, updatedData);
    }

    @Operation(
            summary = "Removes a patient from the system",
            description = "Takes in the patient's database ID and removes the associated data in the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Patient deleted",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> deletePatient(
            @Parameter(
                    name = "id",
                    description = "The patient's database id",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return service.delete(id);
    }
}
