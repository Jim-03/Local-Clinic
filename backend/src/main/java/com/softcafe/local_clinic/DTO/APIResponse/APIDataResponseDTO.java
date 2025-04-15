package com.softcafe.local_clinic.DTO.APIResponse;

import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Data response", description = "An API response with a status, message and data")
public record APIDataResponseDTO(
        @Schema(name = "status", description = "The response status", example = "SUCCESS")
        Status status,
        @Schema(name = "message", description = "A description of the response", example = "Data found")
        String message,
        @Schema(name = "data", description = "Any data to be fetched")
        Object data
) {
}
