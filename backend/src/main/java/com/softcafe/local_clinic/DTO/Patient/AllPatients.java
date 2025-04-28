package com.softcafe.local_clinic.DTO.Patient;

import io.swagger.v3.oas.annotations.media.Schema;

public record AllPatients(
        @Schema(description = "The page number to fetch", example = "1")
        int page
        ) {

}
