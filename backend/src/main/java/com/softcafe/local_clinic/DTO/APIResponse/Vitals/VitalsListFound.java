package com.softcafe.local_clinic.DTO.APIResponse.Vitals;

import com.softcafe.local_clinic.Services.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "An object that containing a status, message and the paginated list of vitals with the total number of pages")
public record VitalsListFound(
        @Schema(description = "The response status", example = "SUCCESS")
        Status status,
        @Schema(description = "The description of the fetch process", example = "Vitals record found")
        String message,
        @Schema(description = "An object with the list of vitals' record and total pages", implementation = PaginatedVitals.class)
        PaginatedVitals data
) {
}
