package com.softcafe.clinic_system.dto.report;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Manager Statistics", description = "Record object for fetching statistics for the manager role")
public record ManagerStats(
        @Schema(description = "The total number of staff members in the system", example = "100")
        long totalStaff,
        @Schema(description = "The total number of staff who are actively on duty", example = "95")
        long staffOnDuty,
        @Schema(description = "The total number of appointments made today", example = "50")
        long dailyAppointments,
        @ArraySchema(
                schema = @Schema(implementation = LogData.class),
                arraySchema = @Schema(description = "A list of the last 5 activities")
        )
        List<LogData> activity
) {
    
}
