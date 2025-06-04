package com.softcafe.clinic_system.dto.record;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object containing a list of records and the total number of pages to be expected")
public record RecordsList(
        @Schema(description = "Total number of pages", example = "5")
        int totalPages,
        @ArraySchema(
                schema = @Schema(implementation = RecordData.class),
                arraySchema = @Schema(description = "Record's data")
        )
        List<RecordData> records
) {
}
