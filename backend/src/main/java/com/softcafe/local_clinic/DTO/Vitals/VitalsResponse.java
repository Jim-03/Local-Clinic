package com.softcafe.local_clinic.DTO.Vitals;

import com.softcafe.local_clinic.Entities.Vitals;
import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Vitals response", description = "A response that fetches vitals data")
public record VitalsResponse(
        @Schema(name = "status", description = "The response status", example = "SUCCESS")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Vitals found")
        String message,
        @Schema(name = "vitals", description = "vital's data", implementation = Vitals.class)
        Object data
) {
}
