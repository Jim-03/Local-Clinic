package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.appointment.AppointmentData;
import com.softcafe.clinic_system.dto.appointment.AppointmentList;
import com.softcafe.clinic_system.dto.appointment.NewAppointment;
import com.softcafe.clinic_system.entities.Appointment;
import com.softcafe.clinic_system.entities.AppointmentStatus;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.Staff;
import com.softcafe.clinic_system.repositories.AppointmentRepository;
import com.softcafe.clinic_system.repositories.PatientRepository;
import com.softcafe.clinic_system.repositories.StaffRepository;
import com.softcafe.clinic_system.utils.AppointmentUtil;
import com.softcafe.clinic_system.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final PatientRepository patientRepository;
    private final int PAGE_SIZE = 10;

    /**
     * Retrieves a list of appointments made by a doctor
     *
     * @param page Page number
     * @param id   Doctor's primary key
     * @return A record object containing the total pages and number of appointments
     * @throws ResponseStatusException: BAD_REQUEST In case of invalid page number or ID
     *                                  NOT_FOUND In case doctor's record wasn't found
     */
    public AppointmentList getByDoctor(int page, Long id) {
        // Validate ID
        Util.validateId(id);
        Util.validatePage(page);

        // Fetch the doctor's details
        Optional<Staff> doctor = staffRepository.findById(id);

        // Check if doctor exists
        if (doctor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified doctor doesn't exist!");
        }

        // Fetch the list of appointments
        Page<Appointment> pages = appointmentRepository.findByDoctor(PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "created_at")), doctor.get());

        List<AppointmentData> list = new ArrayList<>();

        // Convert each entry to a DTO
        for (Appointment appointment : pages) {
            list.add(AppointmentUtil.toDto(appointment));
        }

        return new AppointmentList(pages.getTotalPages(), list);
    }

    /**
     * Retrieves a list of appointments made in a certain period
     *
     * @param start Starting of date range
     * @param end   ENd of date range
     * @param page  Page number
     * @return A record object containing the list of appointments and total number of expected pages
     * @throws ResponseStatusException: BAD_REQUEST In case of invalid page number or dates
     */
    public AppointmentList getByDateRange(LocalDateTime start, LocalDateTime end, int page) {
        Page<Appointment> pages = null;
        pages = page == 0 ?
                appointmentRepository.findByCreatedAtBetween(start, end, Pageable.unpaged()) :
                appointmentRepository.findByCreatedAtBetween(start, end, PageRequest.of(page - 1, PAGE_SIZE));
        List<AppointmentData> list = new ArrayList<>();

        for (Appointment appointment : pages) {
            list.add(AppointmentUtil.toDto(appointment));
        }

        return new AppointmentList(pages.getTotalPages(), list);
    }

    /**
     * Retrieves appointments scheduled with a patient
     *
     * @param id   Patient's primary key
     * @param page Page number
     * @return A record object containing the list of appointments and total number of expected pages
     * @throws ResponseStatusException: BAD_REQUEST In case of invalid page number or ID
     *                                  NOT_FOUND In case patient's details weren't found
     */
    public AppointmentList getByPatient(Long id, int page) {
        Util.validateId(id);
        Util.validatePage(page);

        // Fetch patient's details
        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified patient doesn't exist!");
        }

        Page<Appointment> appointmentPage = appointmentRepository.findByPatient(patient.get(), PageRequest.of(page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "created_at")));

        List<AppointmentData> appointments = new ArrayList<>();

        for (Appointment appointment : appointmentPage) {
            appointments.add(AppointmentUtil.toDto(appointment));
        }

        return new AppointmentList(appointmentPage.getTotalPages(), appointments);
    }

    /**
     * Adds a new appointment to the system
     *
     * @param dto The new appointment record DTO
     * @return The new appointment data
     * @throws ResponseStatusException: BAD_REQUEST In case the IDs are invalid or null
     *                                  NOT_FOUND In case the patient/doctor records weren't found
     */
    @Transactional
    public AppointmentData add(NewAppointment dto) {
        // Check if data is provided
        Util.validateId(dto.patientId());
        Util.validateId(dto.doctorId());

        // Fetch  details
        Optional<Patient> patient = patientRepository.findById(dto.patientId());
        Optional<Staff> doctor = staffRepository.findById(dto.doctorId());

        // Check if they exist
        if (patient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified patient doesn't exist!");
        }

        if (doctor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified doctor wasn't found!");
        }

        //Create an appointment object
        Appointment appointment = new Appointment();
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());
        appointment.setStatus(AppointmentStatus.PENDING);

        AppointmentData appointmentData = AppointmentUtil.toDto(appointmentRepository.save(appointment));

        log.info("A new appointment with ID: {} was created", appointmentData.id());
        return appointmentData;
    }

    /**
     * Updates an existing appointment details
     *
     * @param id      Appointment's primary key
     * @param updated The updated appointment data
     * @return The newly updated data
     * @throws ResponseStatusException: BAD_REQUEST In case of invalid or missing IDs
     *                                  NOT_FOUND In case appointment/doctor/patient details aren't found
     */
    @Transactional
    public AppointmentData update(Long id, NewAppointment updated) {
        Util.validateId(id);
        Util.validateId(updated.patientId());
        Util.validateId(updated.doctorId());

        // Fetch the details
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        Optional<Patient> patient = patientRepository.findById(updated.patientId());
        Optional<Staff> doctor = staffRepository.findById(updated.doctorId());

        // Check if they exist
        if (appointment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified appointment doesn't exist!");
        }

        if (patient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified patient doesn't exist!");
        }

        if (doctor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified doctor doesn't exist!");
        }

        // Update the data
        appointment.get().setDoctor(doctor.get());
        appointment.get().setPatient(patient.get());

        AppointmentData appointmentData = AppointmentUtil.toDto(appointmentRepository.save(appointment.get()));

        log.info("Appointment with ID: {} was updated", appointmentData.id());
        return appointmentData;
    }

    /**
     * Removes an appointment from the system
     *
     * @param id The appointment's primary key
     * @throws ResponseStatusException: BAD_REQUEST In case of missing or invalid id
     *                                  NOT_FOUND In case the specified appointment record wasn't found
     */
    public void delete(Long id) {
        Util.validateId(id);

        // Fetch the appointment's details
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        // Check if appointment exists
        if (appointment.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified appointment doesn't exist!");
        }

        appointmentRepository.delete(appointment.get());
    }

    /**
     * Retrieves a paginated list of appointments
     *
     * @param page The page number to request
     * @return An object containing the total number of expected pages and a list of 10 appointments
     */
    public AppointmentList getAll(int page) {
        Page<Appointment> appointmentPage = appointmentRepository.findAll(PageRequest.of(
                page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt")
        ));

        return new AppointmentList(appointmentPage.getTotalPages(), appointmentPage.stream().map(AppointmentUtil::toDto).toList());
    }
}