package com.softcafe.local_clinic.Controllers;

import com.softcafe.local_clinic.DTO.APIRequest.Vitals.GetByDate;
import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.Vitals.VitalsListFound;
import com.softcafe.local_clinic.DTO.Vitals.NewVitals;
import com.softcafe.local_clinic.DTO.Vitals.VitalsNotFoundResponse;
import com.softcafe.local_clinic.DTO.Vitals.VitalsResponse;
import com.softcafe.local_clinic.Services.VitalsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vitals")
@RequiredArgsConstructor
@Tag(name = "Vitals controller", description = "A controller handling CRUD operations on the vitals model")
public class VitalsController {
    private final VitalsService service;

    @Operation(
            summary = "Vitals in a record",
            description = "Retrieves a vitals data from a specified record"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Vitals found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid record id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Vitals/Record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            )
    })
    @GetMapping("/record/{id}")
    public ResponseEntity<APIDataResponseDTO> getByRecord(
            @Parameter(
                    description = "The record's primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return service.getByRecord(id);
    }

    @Operation(summary = "Get by range", description = "Fetches a paginated list of vitals made between a date range")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Vitals found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VitalsListFound.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing date ranges",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VitalsNotFoundResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Vitals data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VitalsNotFoundResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VitalsNotFoundResponse.class)
                    )
            )
    })
    @PostMapping("/date")
    public ResponseEntity<APIDataResponseDTO> getByDateRange(
            @org.springframework.web.bind.annotation.RequestBody GetByDate date
    ) {
        System.out.println(date);
        return service.getByDate(date);
    }

    @Operation(
            summary = "Add vitals",
            description = "Adds new vitals to a record"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Vitals created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing vitals data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Record not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<APIInfoResponseDTO> addVitals(
            @RequestBody(
                    description = "The new vitals data",
                    content = @Content(
                            schema = @Schema(
                                    implementation = NewVitals.class
                            )
                    ),
                    required = true
            )
            @org.springframework.web.bind.annotation.RequestBody NewVitals vitals
    ) {
        return service.add(vitals);
    }

    @Operation(
            summary = "Update vitals",
            description = "Updates an exiting vitals data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Vitals updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid vitals id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Vitals not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> updateVitals(
            @Parameter(
                    description = "The vitals primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody(
                    description = "The newly updated data",
                    content = @Content(
                            schema = @Schema(
                                    implementation = NewVitals.class
                            )
                    ),
                    required = true
            )
            @org.springframework.web.bind.annotation.RequestBody NewVitals update
    ) {
        return service.update(id, update);
    }

    @Operation(
            summary = "Fetch vitals",
            description = "Retrieves a vitals details by its primary key"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Vitals retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid vitals id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Vitals not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = VitalsNotFoundResponse.class
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIDataResponseDTO> getById(
            @Parameter(
                    description = "The vitals primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return service.getById(id);
    }

    @Operation(
            summary = "Delete vitals",
            description = "Deleted a vitals data from the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Vitals deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid vitals id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Vitals not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = APIInfoResponseDTO.class
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> deleteVitals(
            @Parameter(
                    description = "The vitals primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        return service.delete(id);
    }
}
