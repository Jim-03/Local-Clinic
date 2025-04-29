package com.softcafe.local_clinic.DTO.APIRequest.Appointment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "A record describing the date ranges")
public record GetByDate (
        @Schema(description = "The starting date ")
        LocalDateTime start,
        @Schema(description = "The ending date")
        LocalDateTime end
){
}
