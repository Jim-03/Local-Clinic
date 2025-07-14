package com.softcafe.clinic_system.dto.report;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Receptionist statistics", description = "A DTO containing statistics for a receptionist role")
public record ReceptionistStats(
        @Schema(description = "The total number of appointments made today", example = "20") long total,
        @Schema(description = "The total number of appointments completed today", example = "5") long complete,
        @Schema(description = "The total number of incomplete appointments in the day", example = "15") long incomplete,
        @ArraySchema(schema = @Schema(implementation = LogData.class), arraySchema = @Schema(description = "The last 5 logs made by the receptionist")) List<LogData> logData) {
}
