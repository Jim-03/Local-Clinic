package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.report.ManagerStats;
import com.softcafe.clinic_system.services.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

        private final StatisticsService statisticsService;

        @Operation(summary = "Get statistics for manager")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Statistics found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerStats.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")))
        })
        @GetMapping("/manager")
        public ResponseEntity<ManagerStats> getManagerReport() {
                return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getForManager());
        }

}
