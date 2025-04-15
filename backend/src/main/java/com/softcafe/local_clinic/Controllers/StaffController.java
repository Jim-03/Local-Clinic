package com.softcafe.local_clinic.Controllers;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.Staff.AuthorizationDTO;
import com.softcafe.local_clinic.DTO.Staff.GetByRoleDTO;
import com.softcafe.local_clinic.DTO.Staff.NewStaffDataDTO;
import com.softcafe.local_clinic.Services.StaffService;
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
@RequestMapping("/api/staff/")
@RequiredArgsConstructor
@Tag(name = "Staff Controller", description = "Endpoints for performing CRUD operations on a staff record")
public class StaffController {
    private final StaffService service;

    @Operation(
            summary = "Fetch a list of staff in the same role",
            description = "Finds a list of staff members and returns them depending on the page number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "List of staff found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing role or id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "List of staff not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            )
    })
    @PostMapping("/role")
    public ResponseEntity<APIDataResponseDTO> getByRole(
            @RequestBody(
                    description = "An object containing the role and page number",
                    content = @Content(schema = @Schema(implementation = GetByRoleDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody GetByRoleDTO role
    ) {
        return service.getByRole(role);
    }

    @Operation(
            summary = "Retrieves a user's data",
            description = "Finds a user's data by their primary key"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Staff data found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Staff data not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<APIDataResponseDTO> getById(
            @Parameter(
                    name = "id",
                    description = "The user's primary key",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return service.getById(id);
    }

    @Operation(
            summary = "Adds a new staff member",
            description = "Adds a new staff member to the system's database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Staff data added",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "The new data violates unique constraint",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<APIInfoResponseDTO> addNewStaff(
            @RequestBody(
                    description = "An object containing the new staff data",
                    content = @Content(
                            schema = @Schema(implementation = NewStaffDataDTO.class)
                    )
            ) @org.springframework.web.bind.annotation.RequestBody NewStaffDataDTO newStaff
    ) {
        return service.add(newStaff);
    }

    @Operation(
            summary = "Authorizes users",
            description = "A login route that takes in user details to authorize them into the system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "User authorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing authorization data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Account not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIDataResponseDTO.class)
                    )
            )
    })
    @PostMapping("/authorize")
    public ResponseEntity<APIDataResponseDTO> authorize(
            @RequestBody(
                    description = "An object with the account's credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody AuthorizationDTO auth
    ) {
        return service.authorize(auth);
    }

    @Operation(
            summary = "Updates staff data",
            description = "Updates an existing user's data in the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Staff data updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing id or updated data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409", description = "The new data violates unique constraint",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> updateStaff(
            @RequestBody(
                    description = "The newly updated data",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NewStaffDataDTO.class)
                    )
            )
            @Parameter(name = "id", description = "The user database id", example = "2")
            @org.springframework.web.bind.annotation.RequestBody NewStaffDataDTO updatedData,
            @PathVariable Long id
    ) {
        return service.update(id, updatedData);
    }

    @Operation(summary = "Deletes a staff member from the system")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Staff data deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Invalid/missing id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "The data wasn't found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = APIInfoResponseDTO.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<APIInfoResponseDTO> deleteStaff(
            @Parameter(
                    name = "id",
                    description = "The account's primary key",
                    example = "1"
            )
            @PathVariable Long id
    ){
        return service.delete(id);
    }
}
