package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.report.LogData;
import com.softcafe.clinic_system.dto.report.ManagerStats;
import com.softcafe.clinic_system.dto.report.ReceptionistStats;
import com.softcafe.clinic_system.entities.Appointment;
import com.softcafe.clinic_system.entities.AppointmentStatus;
import com.softcafe.clinic_system.entities.Log;
import com.softcafe.clinic_system.entities.StaffStatus;
import com.softcafe.clinic_system.repositories.AppointmentRepository;
import com.softcafe.clinic_system.repositories.LogRepository;
import com.softcafe.clinic_system.repositories.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StaffRepository staffRepository;
    private final AppointmentRepository appointmentRepository;
    private final LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
    private final LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
    private final LogRepository logRepository;

    /**
     * Retrieves statistics for the manager role
     *
     * @return An object containing the statistics for manager
     */
    public ManagerStats getForManager() {
        long totalStaff = staffRepository.count();
        long dailyAppointments = appointmentRepository.findByCreatedAtBetween(startOfDay, endOfDay, Pageable.unpaged())
                .getTotalElements();
        long staffOnDuty = staffRepository.findByStatus(StaffStatus.ON_DUTY, Pageable.unpaged()).getTotalElements();
        Page<Log> logPage = logRepository.findAll(PageRequest.of(0, 5, Sort.by(
                Sort.Direction.DESC, "time")));
        List<LogData> logList = new ArrayList<>();

        for (Log log : logPage) {
            logList.add(new LogData(log.getId(), log.getAction(), log.getTime()));
        }

        return new ManagerStats(totalStaff, staffOnDuty, dailyAppointments, logList);
    }

    /**
     * Retrieves statistics for the receptionist role
     *
     * @param id Receptionist's primary key
     * @return A statistics object containing appointments data (total, incomplete, complete) and the receptionists logs
     */
    public ReceptionistStats getForReceptionist(long id) {
        Page<Appointment> todayAppointments = appointmentRepository.findByCreatedAtBetweenAndReceptionist_Id(
                startOfDay, endOfDay, id, Pageable.unpaged()
        );
        Page<Log> logPage = logRepository.findByStaff_Id(id, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time")));
        return new ReceptionistStats(
                todayAppointments.getTotalElements(),
                todayAppointments.stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETE).count(),
                todayAppointments.stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.PENDING).count(),
                logPage.stream().map(log -> new LogData(log.getId(), log.getAction(), log.getTime())).toList()
        );
    }
}
