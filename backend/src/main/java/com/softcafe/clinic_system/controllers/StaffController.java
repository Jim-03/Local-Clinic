package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.staff.*;
import com.softcafe.clinic_system.entities.Role;
import com.softcafe.clinic_system.services.StaffService;
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

@RestController
@RequestMapping("/api/staff")
@Tag(name = "Staff Controller", description = "Endpoints handling the staff data")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @Operation(description = "Retrieves a list of doctors on duty")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of doctors found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ListOfStaff.class)
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
    @GetMapping("/doctors/active")
    private ResponseEntity<ListOfStaff> getActiveDoctors() {
        return ResponseEntity.status(HttpStatus.OK).body(staffService.getDoctorOnDuty());
    }

    @Operation(summary = "Search and sort/filter")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Staff list found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ListOfStaff.class)
                    )),
            @ApiResponse(
                    responseCode = "400", description = "Invalid parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide page number!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Specified staff doesn't exist",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"No staff with the specified phone number exists!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
                    )
            )
    })
    @GetMapping("/get")
    public ResponseEntity<ListOfStaff> searchFilterAndSort(
            @RequestParam(value = "identifier", required = false) String identifier,
            @RequestParam(value = "value", required = false) String value,
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam("page") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(staffService.searchSortAndFilter(
                identifier, value, filter, sort, page
        ));
    }


    @Operation(summary = "Get all staff", description = "Retrieves an array of staff members")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of staff found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ListOfStaff.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid or missing page",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid page number\"}")
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
    @GetMapping("/page/{page}")
    public ResponseEntity<ListOfStaff> all(
            @Parameter(
                    description = "Page number",
                    example = "1",
                    required = true
            )
            @PathVariable int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(staffService.getAll(page));
    }

    @Operation(summary = "Authenticate users")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Successful login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StaffData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide at least one identifier\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "403", description = "Incorrect password",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Incorrect password!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Account not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Account not found!\"}")
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
    @PostMapping("/authenticate")
    public ResponseEntity<StaffData> login(@RequestBody StaffCredentials credentials) {
        return ResponseEntity.status(HttpStatus.OK).body(staffService.authenticate(credentials));
    }

    @Operation(summary = "New staff", description = "Adds a new staff member to the system")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Staff record created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StaffData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing staff data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the staff's data!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Duplicate values",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"A staff member with this email already exists\"}")
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
    @PostMapping
    public ResponseEntity<StaffData> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The new staff data",
                    content = @Content(
                            schema = @Schema(implementation = NewStaff.class)
                    )
            )
            @RequestBody NewStaff newStaff
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.addStaff(newStaff));
    }

    @Operation(summary = "Update staff")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Staff data successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StaffData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid id or new data. The new data may also be invalid",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Staff data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified staff member doesn't exist!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "Duplicate data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"A staff member with this email already exists!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error has occurred!\"}")
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<StaffData> update(
            @Parameter(
                    description = "Primary key",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Newly updated data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateStaff.class))
            )
            @RequestBody UpdateStaff updatedData
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(staffService.update(id, updatedData.updatedData(), updatedData.oldPassword()));
    }

    @Operation(summary = "Removes a staff member")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Staff data successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Staff data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified staff member doesn't exist\"}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Primary key",
                    example = "1",
                    required = true
            )
            @PathVariable Long id
    ) {
        staffService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
