package com.softcafe.local_clinic.DTO.APIResponse.Record;

import com.softcafe.local_clinic.Entities.Record;
import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "Record List Response", description = "An API response with a status, message and a list of records")
public record RecordList(
        @Schema(name = "status", description = "The response status", example = "SUCCESS")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Records found")
        String message,
        @ArraySchema(
                schema = @Schema(implementation = Record.class),
                arraySchema = @Schema(description = "List of patient records")
        )
        List<Record> records
) {}