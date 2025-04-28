package com.softcafe.local_clinic.DTO.APIResponse.Patient;

import java.util.List;

import com.softcafe.local_clinic.Entities.Patient;
import com.softcafe.local_clinic.Services.Status;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Record List Response", description = "An API response with a status, message and a list of records")
public record PatientListFound(
        @Schema(name = "status", description = "The response status", example = "SUCCESS")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Patients found")
        String message,
        @ArraySchema(
                schema = @Schema(implementation = Patient.class),
                arraySchema = @Schema(description = "List of patients")
        )
        List<com.softcafe.local_clinic.DTO.Patient.PatientDataDTO> patients,
        @Schema(name = "total pages", description = "The total number of pages to expect from the list", example = "20")
        int totalPages
        ) {

}
