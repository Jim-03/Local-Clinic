package com.softcafe.clinic_system.dto.billing;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object returned from an operation resulting to a list of bills")
public record BillList(
        @Schema(description = "Total number of pages to be expected from the fetch operation")
        int totalPages,
        @ArraySchema(
                schema = @Schema(implementation = BillingData.class),
                arraySchema = @Schema(description = "A list of billings")
        )
        List<BillingData> bills
) {
}
