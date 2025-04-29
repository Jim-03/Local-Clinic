package com.softcafe.local_clinic.DTO.APIRequest.Vitals;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "A dto describing the object that requests for vitals in a specified date range")
public record GetByDate(
        LocalDateTime start,
        LocalDateTime end,
        int page
) {
}
