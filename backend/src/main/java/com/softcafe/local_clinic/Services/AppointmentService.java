package com.softcafe.local_clinic.Services;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.Entities.Appointment;
import com.softcafe.local_clinic.Entities.AppointmentStatus;
import com.softcafe.local_clinic.Repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository repository;

    /**
     * Retrieves a list of incomplete appointments
     * @return A Response Entity with a body containing a list of incomplete appointments
     */
    public ResponseEntity<APIDataResponseDTO> findIncomplete() {
        try {
            // Fetch a list of appointments that are incomplete
            List<Appointment> incompleteAppointments = repository.findByStatus(AppointmentStatus.INCOMPLETE);

            // Check if list is empty
            if (incompleteAppointments.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "No incomplete appointments", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Incomplete appointments found", incompleteAppointments);
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the list of incomplete appointments: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Retrieves a list of appointments made between two date ranges
     * @param date A DTO containing a starting date and ending date
     * @return A Response Entity with a body containing the list of appointments or null
     */
    public ResponseEntity<APIDataResponseDTO> getByDate(GetByDate date) {
        // Check if the dates are provided
        if (date.start() == null || date.end() == null) {
            return Responses.dataResponse(Status.REJECTED, "Provide the date ranges", 0);
        }

        try {
            // Fetch the number of appointments
            List<Appointment> appointmentList = repository.findByCreatedAtBetween(date.start(), date.end());

            return Responses.dataResponse(Status.SUCCESS, "List of appointments found", appointmentList);
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the appointments by date range: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred", 0);
        }
    }
}
