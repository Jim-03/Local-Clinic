package com.softcafe.local_clinic.DTO.APIResponse.Record;

import com.softcafe.local_clinic.Entities.Record;
import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Record Response", description = "An API response with a status, message and a record's data")
public record RecordResponse(
        @Schema(name = "status", description = "The response status", example = "SUCCESS")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Record found")
        String message,
        @Schema(name = "data", description = "The record's data", implementation = Record.class)
        Record record
) {
}
