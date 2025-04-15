package com.softcafe.local_clinic.DTO.APIResponse;

import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Informatory response", description = "An API response with a status and message")
public record APIInfoResponseDTO(
        @Schema(name = "status", description = "The status of the response", example = "NOT_FOUND")
        Status status,
        @Schema(name = "message", description = "An informatory message about the response", example = "Data not found!")
        String message
) {
}
