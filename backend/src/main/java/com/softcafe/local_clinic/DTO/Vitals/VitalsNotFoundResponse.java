package com.softcafe.local_clinic.DTO.Vitals;

import com.softcafe.local_clinic.Entities.Vitals;
import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Vitals response", description = "A response that fetches vitals data")
public record VitalsNotFoundResponse(
        @Schema(name = "status", description = "The response status", example = "NOT_FOUND")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Vitals not found")
        String message,
        @Schema(name = "vitals", description = "vital's data", example = "null")
        Object data
) {
}
