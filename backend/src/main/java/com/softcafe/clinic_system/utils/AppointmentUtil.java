package com.softcafe.clinic_system.utils;

import com.softcafe.clinic_system.dto.appointment.AppointmentData;
import com.softcafe.clinic_system.entities.Appointment;

public class AppointmentUtil {

    /**
     * Converts an appointment data to its DTO
     *
     * @param appointment Appointment data
     * @return Appointment DTO
     */
    public static AppointmentData toDto(Appointment appointment) {
        return new AppointmentData(
                appointment.getId(),
                PatientUtil.toDto(appointment.getPatient()),
                appointment.getDoctor() == null ? null : StaffUtil.toDto(appointment.getDoctor()),
                appointment.getStatus(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}
