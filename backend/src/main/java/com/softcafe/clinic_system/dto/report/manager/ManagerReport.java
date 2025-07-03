package com.softcafe.clinic_system.dto.report.manager;

import com.softcafe.clinic_system.dto.appointment.ManagerAppointmentReport;
import com.softcafe.clinic_system.dto.report.doctor.DoctorsReport;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "An object containing report data for a manager")
public record ManagerReport(
        @Schema(description = "Appointments data object", implementation = ManagerAppointmentReport.class)
        ManagerAppointmentReport appointments,
        @ArraySchema(
                schema = @Schema(implementation = DoctorsReport.class),
                arraySchema = @Schema(description = "A list of doctors' report")
        )
        List<DoctorsReport> doctors,
        @Schema(description = "Revenue data object", implementation = RevenueData.class)
        RevenueData revenue
) {
}
