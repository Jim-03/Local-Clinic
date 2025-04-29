package com.softcafe.local_clinic.DTO.APIResponse.Vitals;

import com.softcafe.local_clinic.Entities.Vitals;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "A paginated list of vitals")
public record PaginatedVitals(
        @ArraySchema(
                schema = @Schema(implementation = Vitals.class),
                arraySchema = @Schema(description = "The list of vitals records")
        )
        List<Vitals> vitals,
        @Schema(description = "The total number of pages")
        int totalPages
) {
}
