package com.softcafe.clinic_system.controllers;

import com.softcafe.clinic_system.dto.billing.BillList;
import com.softcafe.clinic_system.dto.billing.BillingData;
import com.softcafe.clinic_system.dto.billing.NewBill;
import com.softcafe.clinic_system.entities.PaymentStatus;
import com.softcafe.clinic_system.services.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/billing")
@Validated
@RequiredArgsConstructor
@Tag(name = "Billing-Controller", description = "Controller having endpoints that handle billing's data")
public class BillingController {
    private final BillingService billingService;

    @Operation(summary = "Fetch by patient", description = "Fetches a list of billings tied to a patient")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "billings found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BillList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid ID or page number",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the patient's ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's details npt found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient wasn't found!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"An error occurred!\"}")
                    )
            )
    })
    @GetMapping("/patient")
    public ResponseEntity<BillList> getByPatient(
            @Parameter(description = "The patient's primary key", example = "1")
            @NotNull @Min(value = 1, message = "Patient ID should be at least 1!")
            @RequestParam("id") Long id,
            @Parameter(description = "Page number", example = "1")
            @NotNull @Min(value = 1, message = "Page number should be at least 1!")
            @RequestParam("page") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(billingService.getByPatient(id, page));
    }

    @Operation(summary = "Fetch by payment method")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Billings found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BillList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid method or page number",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the payment method!\"}")
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
    @GetMapping("/status")
    public ResponseEntity<BillList> getByMethod(
            @Parameter(description = "Payment method to search for", example = "cash")
            @NotNull(message = "Provide the payment method!")
            @RequestParam("status") String method,
            @Parameter(description = "Page number", example = "1")
            @NotNull(message = "Provide the page number") @Min(value = 1, message = "Page number should be at least 1!")
            @RequestParam("page") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(billingService.getByPaymentMethod(method, page));
    }

    @Operation(summary = "Fetch by payment status")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Billings found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BillList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid status or page number",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the payment status!\"}")
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
    @GetMapping("/status")
    public ResponseEntity<BillList> getByStatus(
            @Parameter(description = "Payment status to search for",
                    schema = @Schema(implementation = PaymentStatus.class))
            @NotNull(message = "Provide the payment status!")
            @RequestParam("status") PaymentStatus status,
            @Parameter(description = "Page number", example = "1")
            @NotNull(message = "Provide the page number") @Min(value = 1, message = "Page number should be at least 1!")
            @RequestParam("page") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(billingService.getByStatus(status, page));
    }

    @Operation(summary = "Get by date range", description = "Retrieve a list of billings made between two date ranges")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Billings found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BillList.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid dates or page number",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the page number!\"}")
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
    @GetMapping
    public ResponseEntity<BillList> getBills(
            @Parameter(description = "The start of date time range", example = "2020-04-12T00:00:00")
            @RequestParam("start") @NotNull LocalDateTime start,
            @Parameter(description = "The end of the date time range", example = "2025-04-09T23:59:59")
            @RequestParam("end") @NotNull LocalDateTime end,
            @Parameter(description = "Page number", example = "1")
            @RequestParam("page") @NotNull @Min(value = 1, message = "Provide the page number!") int page
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(billingService.getByDateRange(start, end, page));
    }

    @Operation(summary = "Adds new billing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Billing successfully saved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BillingData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid billing data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the new billing's data!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's details not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient wasn't found!\"}")
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
    @PostMapping
    public ResponseEntity<BillingData> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New billing object data",
                    content = @Content(schema = @Schema(implementation = NewBill.class)),
                    required = true
            )
            @NotNull(message = "Provide the new billing's data!") @RequestBody NewBill newBill
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billingService.add(newBill));
    }

    @Operation(summary = "Updates an existing billing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200", description = "Billing successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BillingData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid billing data or id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide the updated billing's data!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Patient's/Billing's details not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified patient wasn't found!\"}")
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
    @PutMapping
    public ResponseEntity<BillingData> update(
            @Parameter(description = "Primary key", example = "1")
            @NotNull @Min(value = 1, message = "ID should be at least 1!")
            @RequestParam("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated billing data",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NewBill.class)
                    )
            )
            @NotNull(message = "Provide the updated billing's data!")
            @RequestBody NewBill update
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(billingService.update(id, update));
    }

    @Operation(summary = "Deletes an existing billing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204", description = "Billing successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "400", description = "Missing/invalid id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Provide a valid ID!\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404", description = "Billing's details not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"The specified billing wasn't found!\"}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Primary key",
                    example = "1",
                    required = true
            )
            @NotNull(message = "Provide a valid ID!") @Min(value = 1, message = "ID should be at least 1!")
            @PathVariable Long id
    ) {
        billingService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
