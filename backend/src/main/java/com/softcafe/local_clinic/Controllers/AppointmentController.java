package com.softcafe.local_clinic.Controllers;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.Appointment.AppointmentListFound;
import com.softcafe.local_clinic.DTO.APIResponse.Appointment.AppointmentListNotFound;
import com.softcafe.local_clinic.Services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments Controller", description = "Handles HTTP requests for the Appointment Model")
public class AppointmentController {

    private final AppointmentService service;

    @Operation(summary = "Incomplete appointments", description = "Retrieves a list of appointments marked as complete")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "The list was found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentListFound.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "The appointment list wasn't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentListNotFound.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentListNotFound.class)
                    )
            )
    })
    @GetMapping("/incomplete")
    public ResponseEntity<APIDataResponseDTO> getIncomplete() {
        return service.findIncomplete();
    }
}
