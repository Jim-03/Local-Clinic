package com.softcafe.local_clinic.DTO.APIRequest.Appointment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "Appointment history object")
public record GetByHistory (
        LocalDateTime start,
        LocalDateTime end,
        int page
){
}
