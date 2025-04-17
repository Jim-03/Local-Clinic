package com.softcafe.local_clinic.DTO.APIResponse.Record;

import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Record Data response", description = "An API response with a status, message and without the record's data")
public record RecordNotFound(
        @Schema(name = "status", description = "The response status", example = "NOT_FOUND")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Record not found")
        String message,
        @Schema(name = "record", description = "No data", example = "null")
        Record record
) {
}
