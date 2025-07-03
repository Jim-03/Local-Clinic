package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.appointment.AppointmentData;
import com.softcafe.clinic_system.dto.appointment.AppointmentList;
import com.softcafe.clinic_system.dto.appointment.NewAppointment;
import com.softcafe.clinic_system.services.AppointmentService;
import com.softcafe.clinic_system.utils.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/appointment")
@RequiredArgsConstructor
@Tag(name = "Appointment Controller", description = "A controller class containing endpoints handling appointment data")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Adds new appointment")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Appointment created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing or invalid patient/doctor ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Doctor or patient record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient wasn't found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @PostMapping
    public ResponseEntity<AppointmentData> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The new appointment data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewAppointment.class))
            )
            @RequestBody NewAppointment newAppointment
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.add(newAppointment));
    }

    @Operation(summary = "Fetch by patient", description = "Fetches a list of appointments the patient has attended")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "The list was found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing ID or page number",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid page number!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient wasn't found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @GetMapping("/patient/{id}/page/{page}")
    public ResponseEntity<AppointmentList> fetchByPatient(
            @Parameter(
                    description = "Patient's primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @Parameter(
                    description = "Page number starting from 1",
                    example = "1",
                    required = true
            )
            @PathVariable int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByPatient(id, page));
    }

    @Operation(summary = "Fetch by doctor", description = "Fetches a list of appointments the doctor has attended")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "The list was found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing ID or page number",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid page number!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified doctor wasn't found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @GetMapping("/doctor/{id}/page/{page}")
    public ResponseEntity<AppointmentList> fetchByDoctor(
            @Parameter(
                    description = "Doctor's primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @Parameter(
                    description = "Page number",
                    example = "1",
                    required = true
            )
            @PathVariable int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByDoctor(page, id));
    }

    @Operation(summary = "Get by creation range", description = "Fetches a list of appointments created between two dates")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of appointments found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing ID/dates",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the starting date\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @GetMapping("/date/{page}")
    public ResponseEntity<AppointmentList> fetchByDateRange(
            @Parameter(
                    description = "Starting date range",
                    schema = @Schema(implementation = LocalDateTime.class),
                    required = true
            )
            @RequestParam("start") LocalDateTime start,
            @Parameter(
                    description = "Ending date range",
                    schema = @Schema(implementation = LocalDateTime.class),
                    required = true
            )
            @RequestParam("end") LocalDateTime end,
            @Parameter(
                    description = "Page number starting from 1",
                    required = true,
                    example = "1"
            )
            @PathVariable int page
    ) {
        Util.validatePage(page);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByDateRange(start, end, page));
    }

    @Operation(summary = "Update appointment")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Appointment updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing or invalid patient/doctor ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Doctor or patient record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient wasn't found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentData> update(
            @Parameter(
                    description = "Appointment primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @Parameter(
                    description = "New appointment data",
                    schema = @Schema(implementation = NewAppointment.class),
                    required = true
            )
            @RequestBody NewAppointment newAppointment
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.update(id, newAppointment));
    }

    @Operation(summary = "Delete appointment")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Appointment deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing or invalid  ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Appointment record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified appointment wasn't found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred\"}")
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentData> delete(
            @Parameter(
                    description = "Appointment's primary key",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        appointmentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
