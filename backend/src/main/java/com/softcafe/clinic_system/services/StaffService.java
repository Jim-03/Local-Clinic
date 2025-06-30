package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.staff.ListOfStaff;
import com.softcafe.clinic_system.dto.staff.NewStaff;
import com.softcafe.clinic_system.dto.staff.StaffCredentials;
import com.softcafe.clinic_system.dto.staff.StaffData;
import com.softcafe.clinic_system.entities.Role;
import com.softcafe.clinic_system.entities.Staff;
import com.softcafe.clinic_system.entities.StaffStatus;
import com.softcafe.clinic_system.repositories.StaffRepository;
import com.softcafe.clinic_system.utils.StaffUtil;
import com.softcafe.clinic_system.utils.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

            // Check for duplicated values
            StaffExists(newStaff);

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
     * Checks if the new staff data violates unique constraints
     *
     * @param newStaff The new staff data
     * @throws ResponseStatusException CONFLICT in case of duplicated values
     */
    private void StaffExists(Staff newStaff) {
        String violated = null;

        if (staffRepository.findByEmail(newStaff.getEmail()).isPresent()) violated = "email";
        if (staffRepository.findByPhone(newStaff.getPhone()).isPresent()) violated = "phone number";
        if (staffRepository.findByNationalId(newStaff.getNationalId()).isPresent()) violated = "national ID number";
        if (staffRepository.findByUsername(newStaff.getUsername()).isPresent()) violated = "username";

        if (violated != null) throw new ResponseStatusException(
                HttpStatus.CONFLICT, "A staff member with the specified " + violated + " already exists!"
        );
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

    /**
     * Allows user to access their data
     *
     * @param credentials The user's account details
     * @return Account data
     * @throws ResponseStatusException: BAD_REQUEST In case of missing credential data
     *                                  NOT_FOUND In case the account wasn't found
     *                                  FORBIDDEN In case of incorrect password
     */
    public StaffData authenticate(StaffCredentials credentials) {
        // Check if at least one identifier is provided
        if (credentials.username() == null && credentials.email() == null && credentials.phone() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide at least one identifier!");
        }

        if (credentials.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the password to your account!");
        }


        // Fetch the account
        Staff staff = staffRepository.findByUsernameOrPhoneOrEmail(credentials.username(), credentials.phone(), credentials.email())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));

        // TODO: JWT authentication
        // Verify the password
        if (!BCrypt.checkpw(credentials.password(), staff.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect password!");
        }

        return StaffUtil.toDto(staff);
    }

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

            StaffUtil.validateUpdatedData(newData);

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

            // Hash the new password if provided
            if (newData.password() != null)
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

    /**
     * Fetches staff data depending on a search, sort or filter
     *
     * @param identifier The staff's email, phone or name
     * @param value      The value of the identifier
     * @param filter     A filter term
     * @param sort       A sorting criteria
     * @param page       Page number
     * @return An object containing a list of staff and the total expected pages
     */
    public ListOfStaff searchSortAndFilter(String identifier, String value, String filter, String sort, int page) {
        Page<Staff> staffPage = null;
        List<StaffData> staffList = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);

        if (sort != null) {
            pageable = switch (sort) {
                case "ascendingDate" -> PageRequest.of(page - 1, PAGE_SIZE, Sort.by("createdAt").ascending());
                case "descendingDate" -> PageRequest.of(page - 1, PAGE_SIZE, Sort.by("createdAt").descending());
                case "lastLogin" -> PageRequest.of(page - 1, PAGE_SIZE, Sort.by("lastLogin").descending());
                default -> throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid sort criteria!"
                );
            };
        }

        // Case 1: Search by email/phone/name
        if (identifier != null && value != null) {
            if (identifier.equalsIgnoreCase("email")) {
                Util.isValidEmail(value);
                Staff staff = staffRepository.findByEmail(value).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "No staff member with the specified email exists!"
                        )
                );
                if (filter != null && !StaffUtil.staffMatchesFilter(staff, filter)) {
                    staffList = new ArrayList<>();
                } else {
                    staffList.add(StaffUtil.toDto(staff));
                }
                return new ListOfStaff(1, staffList);
            } else if (identifier.equalsIgnoreCase("phone")) {
                Util.isValidPhone(value);
                Staff staff = staffRepository.findByPhone(value).orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "No staff member with the specified phone exists!"
                        )
                );
                if (filter != null && !StaffUtil.staffMatchesFilter(staff, filter)) {
                    staffList = new ArrayList<>();
                } else {
                    staffList.add(StaffUtil.toDto(staff));
                }
                return new ListOfStaff(1, staffList);
            } else if (identifier.equalsIgnoreCase("name")) {
                if (filter != null) {
                    if (List.of("NURSE", "DOCTOR", "PHARMACIST", "TECHNICIAN", "RECEPTIONIST").contains(filter)) {
                        staffPage = staffRepository.findByFullNameContainingIgnoreCaseAndRole(
                                value, Role.valueOf(filter), pageable);
                    } else if (List.of("ON_DUTY", "OFF", "SUSPENDED").contains(filter)) {
                        staffPage = staffRepository.findByFullNameContainingIgnoreCaseAndStatus(
                                value, StaffStatus.valueOf(filter), pageable);
                    }
                } else {
                    staffPage = staffRepository.findByFullNameContainingIgnoreCase(value, pageable);
                }
            }
        }
        // Case 2: Filter only (no search)
        else if (filter != null) {
            if (List.of("NURSE", "DOCTOR", "PHARMACIST", "TECHNICIAN", "RECEPTIONIST").contains(filter)) {
                staffPage = staffRepository.findByRole(Role.valueOf(filter), pageable);
            } else if (List.of("ON_DUTY", "OFF", "SUSPENDED").contains(filter)) {
                staffPage = staffRepository.findByStatus(StaffStatus.valueOf(filter), pageable);
            }
        }
        // Case 3: No search or filter, just sorting/pagination
        else {
            staffPage = staffRepository.findAll(pageable);
        }

        if (staffPage != null) {
            List<StaffData> finalStaffList = staffList;
            staffPage.getContent().forEach(staff -> finalStaffList.add(StaffUtil.toDto(staff)));
            return new ListOfStaff(staffPage.getTotalPages(), staffList);
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid query parameters!"
        );
    }
}
