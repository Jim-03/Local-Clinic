package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.report.manager.ManagerReport;
import com.softcafe.clinic_system.dto.report.manager.RevenueData;
import com.softcafe.clinic_system.dto.appointment.ManagerAppointmentReport;
import com.softcafe.clinic_system.dto.report.doctor.DoctorsReport;
import com.softcafe.clinic_system.entities.*;
import com.softcafe.clinic_system.repositories.AppointmentRepository;
import com.softcafe.clinic_system.repositories.BillingRepository;
import com.softcafe.clinic_system.repositories.StaffRepository;
import com.softcafe.clinic_system.utils.BillingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final BillingRepository billingRepository;

    /**
     * Fetches a report for the manager role
     *
     * @param start Start of date range
     * @param end   End of date range
     * @return An object providing the data to the report
     */
    public ManagerReport getManager(LocalDateTime start, LocalDateTime end) {
        validateDate(start, end);

        // Fetch data
        List<Appointment> appointments = appointmentRepository.findByCreatedAtBetween(start, end, Pageable.unpaged()).getContent();
        List<Staff> doctors = staffRepository.findByRole(Role.DOCTOR, Pageable.unpaged()).getContent();
        List<Billing> billings = billingRepository.findByCreatedAtBetween(start, end, Pageable.unpaged()).getContent();

        // Create a map of appointment IDs to their doctor IDs for quick lookup
        Map<Long, Long> appointmentDoctorMap = appointments.stream()
                .collect(Collectors.toMap(
                        Appointment::getId,
                        appointment -> appointment.getDoctor().getId()
                ));

        // Appointment report
        Map<AppointmentStatus, Long> appointmentCounts = appointments.stream()
                .collect(Collectors.groupingBy(
                        Appointment::getStatus,
                        Collectors.counting()
                ));

        ManagerAppointmentReport managerAppointmentReport = new ManagerAppointmentReport(
                appointments.size(),
                appointmentCounts.getOrDefault(AppointmentStatus.COMPLETE, 0L),
                appointmentCounts.getOrDefault(AppointmentStatus.PENDING, 0L),
                appointmentCounts.getOrDefault(AppointmentStatus.CANCELLED, 0L)
        );

        // Doctor reports
        List<DoctorsReport> doctorsReportList = doctors.stream()
                .map(doctor -> {
                    // Get doctor
                    Long doctorId = doctor.getId();

                    // Count appointments for this doctor
                    long completed = appointments.stream()
                            .filter(a -> a.getDoctor().getId().equals(doctorId) &&
                                    a.getStatus() == AppointmentStatus.COMPLETE)
                            .count();

                    long cancelled = appointments.stream()
                            .filter(a -> a.getDoctor().getId().equals(doctorId) &&
                                    a.getStatus() == AppointmentStatus.CANCELLED)
                            .count();

                    // Calculate revenue from billings for this doctor's appointments
                    double revenue = billings.stream()
                            .filter(b -> {
                                Long billingAppointmentId = b.getAppointment().getId();
                                Long associatedDoctorId = appointmentDoctorMap.get(billingAppointmentId);
                                return doctorId.equals(associatedDoctorId);
                            })
                            .mapToDouble(b -> BillingUtil.toDto(b).amountPaid())
                            .sum();

                    return new DoctorsReport(
                            doctorId,
                            doctor.getFullName(),
                            completed,
                            cancelled,
                            revenue
                    );
                })
                .toList();

        // Revenue calculations
        double totalPaidRevenue = billings.stream()
                .mapToDouble(b -> BillingUtil.toDto(b).amountPaid())
                .sum();

        double totalExpectedRevenue = billings.stream()
                .mapToDouble(b -> BillingUtil.toDto(b).totalAmount())
                .sum();


        RevenueData revenueData = new RevenueData(
                totalPaidRevenue,
                totalExpectedRevenue
        );

        return new ManagerReport(managerAppointmentReport, doctorsReportList, revenueData);
    }

    /**
     * Checks if the date ranges are valid
     * @param start Starting date
     * @param end Ending date
     */
    private void validateDate(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter valid start and end dates!");
        }
    }
}