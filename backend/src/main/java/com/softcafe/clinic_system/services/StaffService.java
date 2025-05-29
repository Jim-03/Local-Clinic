package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.staff.ListOfStaff;
import com.softcafe.clinic_system.dto.staff.NewStaff;
import com.softcafe.clinic_system.dto.staff.StaffData;
import com.softcafe.clinic_system.entities.Role;
import com.softcafe.clinic_system.entities.Staff;
import com.softcafe.clinic_system.repositories.StaffRepository;
import com.softcafe.clinic_system.utils.StaffUtil;
import com.softcafe.clinic_system.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final int PAGE_SIZE = 10;

    /**
     * Adds a new staff member to the database
     *
     * @param staff The new staff's data
     * @return The saved database object
     * @throws ResponseStatusException BAD_REQUEST In case of invalid or missing input data
     * @throws ResponseStatusException CONFLICT In case of duplicated data
     */
    @Transactional
    public StaffData addStaff(NewStaff staff) {
        // Check if staff data exists
        if (staff == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the staff's data!");
        }

        try {
            // Validate the data
            StaffUtil.validate(staff);

            // Convert to staff object
            Staff newStaff = StaffUtil.toStaff(staff);

            // Hash the password
            newStaff.setPassword(BCrypt.hashpw(staff.password(), BCrypt.gensalt(10)));

            StaffData staffData = StaffUtil.toDto(staffRepository.save(newStaff));
            log.info("A staff member with ID: {} has been created", staffData.id());

            return staffData;
        } catch (DataIntegrityViolationException e) {
            String violated = Util.parseViolation(e);

            String message = violated == null ?
                    "A staff member with this data already exists!" :
                    "A staff member with this " + violated + " already exists!";

            log.warn(message);
            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Retrieves a list of staff members in the same role
     *
     * @param role The role of the staff
     * @param page The page number
     * @return An object containing the list of staff members and the total pages
     * @throws ResponseStatusException BAD_REQUEST In case of null or invalid parameters
     */
    public ListOfStaff getByRole(Role role, int page) {
        // Check if role is provided
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid role!");
        }

        // Check if page number is valid
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid page number!");
        }
        // Fetch the list of members
        Page<Staff> staffPage = staffRepository.findByRole(role, PageRequest.of(page - 1, PAGE_SIZE));

        int totalPages = staffPage.getTotalPages();

        List<StaffData> staffDataList = new ArrayList<>();

        // Convert the data objects to dto
        for (Staff staff : staffPage) {
            staffDataList.add(StaffUtil.toDto(staff));
        }

        log.info("A list of {} was fetched generating a total of {} pages", role, totalPages);
        return new ListOfStaff(totalPages, staffDataList);
    }

    /**
     * Retrieves a list of all staff members
     *
     * @param page The page number to fetch from
     * @return A record containing the total number of pages and the list of staff members
     * @throws ResponseStatusException BAD_REQUEST In case of an invalid page number
     */
    public ListOfStaff getAll(int page) {
        // Validate the page
        if (page <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid page!");
        }

        Page<Staff> staffPage = staffRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE));

        int totalPages = staffPage.getTotalPages();

        List<StaffData> staffDataList = new ArrayList<>();

        for (Staff staff : staffPage) {
            staffDataList.add(StaffUtil.toDto(staff));
        }
        return new ListOfStaff(totalPages, staffDataList);
    }

    // TODO: JWT authentication

    /**
     * Updates an existing staff member's data
     *
     * @param id          The primary key
     * @param newData     The newly updated data
     * @param oldPassword The account's old password
     * @return The newly updated object data
     */
    public StaffData update(Long id, NewStaff newData, String oldPassword) {
        try {
            // Validate parameters
            Util.validateId(id);

            if (newData == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the updated data!");
            }

            if (oldPassword.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the old password!");
            }

            StaffUtil.validate(newData);

            // Fetch the old data
            Optional<Staff> staff = staffRepository.findById(id);

            // Check if staff exists
            if (staff.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified staff member doesn't exist!");
            }

            Staff oldData = staff.get();

            // Check if old password is correct
            if (!BCrypt.checkpw(oldPassword, oldData.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password!");
            }

            // Update the staff data
            StaffUtil.update(oldData, newData);

            // Hash the new password
            oldData.setPassword(BCrypt.hashpw(newData.password(), BCrypt.gensalt(10)));

            StaffData data = StaffUtil.toDto(staffRepository.save(oldData));

            log.info("Account with ID: {} was updated", data.id());

            return data;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DataIntegrityViolationException e) {
            String violated = Util.parseViolation(e);

            String message = violated == null ?
                    "A staff member with these details already exists!" :
                    "A staff member with this " + violated + " already exists!";

            log.warn(message);

            throw new ResponseStatusException(HttpStatus.CONFLICT, message);
        }
    }

    /**
     * Removes a staff member from the system
     *
     * @param id The Primary key
     * @throws ResponseStatusException BAD_REQUEST in case of missing ID
     * @throws ResponseStatusException NOT_FOUND in case the staff data wasn't found
     */
    public void delete(Long id) {
        // Validat the ID
        Util.validateId(id);

        // Fetch the data
        Optional<Staff> staff = staffRepository.findById(id);

        // Check if staff member exists
        if (staff.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified staff member doesn't exist!");
        }

        staffRepository.delete(staff.get());

        log.info("Staff member with ID {} was removed", id);
    }
}