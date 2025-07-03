package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.report.manager.ManagerReport;
import com.softcafe.clinic_system.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Tag(name = "Report Controller", description = "Exposes endpoints that provide reports")
public class ReportsController {
    private final ReportService service;

    @Operation(description = "Fetches a report for the manager role")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Report found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ManagerReport.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid dates",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Enter valid date ranges!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @GetMapping("/manager")
    public ResponseEntity<ManagerReport> getManagerReport(
            @RequestParam(name = "start") LocalDateTime start,
            @RequestParam(name = "end") LocalDateTime end
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getManager(start, end));
    }
}
