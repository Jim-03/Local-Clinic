package com.softcafe.local_clinic.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import com.softcafe.local_clinic.DTO.Staff.AuthorizationDTO;
import com.softcafe.local_clinic.DTO.Staff.NewStaffDataDTO;
import com.softcafe.local_clinic.DTO.Staff.StaffDataDTO;
import com.softcafe.local_clinic.Entities.Gender;
import com.softcafe.local_clinic.Entities.User;
import com.softcafe.local_clinic.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.Staff.GetByRoleDTO;
import com.softcafe.local_clinic.Entities.Staff;
import com.softcafe.local_clinic.Entities.StaffRole;
import com.softcafe.local_clinic.Repositories.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves a list of staff members in the same role
     * @param role An object containing:
     *             role: The role of the staff members
     *             page: The page number
     * @return A Response Entity with a body containing the list of staff members or null
     */
    public ResponseEntity<APIDataResponseDTO> getByRole(GetByRoleDTO role) {
        // Check if the object has all valid data
        if (role.role() == null || role.role().isEmpty()) {
            return Responses.dataResponse(Status.REJECTED, "Provide the staff role!", null);
        }
        if (role.page() <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid page number!", null);
        }

        try {
            // Fetch the list of staff members
            List<Staff> staff = staffRepository.findByRole(
                    StaffRole.valueOf(role.role().toUpperCase()),
                    (Pageable) PageRequest.of(role.page() - 1, 10)
            );

            // Check if the staff exist
            if (staff == null || staff.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The specified role doesn't have any staff members", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Staff found", staff);
        } catch (Exception e) {
            System.err.println("An error has occurred while fetching the staff list: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Retrieves a staff by their primary key
     * @param id The user's primary key
     * @return A Response Entity with a body containing the user's data or null
     */
    public ResponseEntity<APIDataResponseDTO> getById(Long id) {
        // Check if id is provided and is valid
        if (id == null || id <= 0) {
            return Responses.dataResponse(Status.REJECTED, "Provide a valid id!", null);
        }

        try {
            // Fetch the staff data
            Optional<Staff> staff = staffRepository.findById(id);

            // Check if staff exists
            if (staff == null || staff.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "Staff not found!", null);
            }

            return Responses.dataResponse(Status.SUCCESS, "Staff found", toDTO(staff.get()));
        } catch (Exception e) {
            System.err.println("An error has occurred when fetching the user's data: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Adds a new staff member to the system
     * @param staffDataDTO The new staff data object
     * @return A Response Entity with a body confirming if the staff member was added
     */
    @Transactional
    public ResponseEntity<APIInfoResponseDTO> add(NewStaffDataDTO staffDataDTO) {
        // Check if the data is provided
        if (staffDataDTO == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the user's data!");
        }

        try {
            // Check if the data violates unique constraint
            if (getStaff(staffDataDTO.username(), staffDataDTO.email(), staffDataDTO.phone()).isPresent()) {
                return Responses.infoResponse(Status.DUPLICATE, "A staff member exists with this credentials!");
            }

            // Encrypt the password
            String password = BCrypt.hashpw(staffDataDTO.password(), BCrypt.gensalt(10));

            // Save the user's data
            staffRepository.save(new Staff(
                    staffDataDTO.username(),
                    password,
                    staffDataDTO.role(),
                    staffDataDTO.department(),
                    staffDataDTO.specialization(),
                    LocalDate.now(),
                    staffDataDTO.isActive(),
                    null
            ));

            return Responses.infoResponse(Status.CREATED, "Staff successfully added");
        } catch (Exception e) {
            System.err.println("An error has occurred while adding the staff: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Authorizes a user to access the system
     * @param auth The account's credentials
     * @return A Response Entity with a body containing an authorization token
     *             TODO: Generate JWT
     */
    public ResponseEntity<APIDataResponseDTO> authorize(AuthorizationDTO auth) {
        // Check if the authorization data is provided
        if (auth == null) {
            return Responses.dataResponse(Status.REJECTED, "Provide the account's credentials", null);
        }

        // Check if one identifier is provided
        if (auth.username() == null && auth.email() == null && auth.phone() == null) {
            return Responses.dataResponse(Status.REJECTED, "Provide at least one identifier!", null);
        }
        try {
            // Fetch the account's details
            Optional<Staff> user = getStaff(auth.username(), auth.email(), auth.phone());

            // Check if user exists
            if (user.isEmpty()) {
                return Responses.dataResponse(Status.NOT_FOUND, "The account wasn't found!", null);
            }

            // Validate password
            if (!BCrypt.checkpw(auth.password(), user.get().getPassword())) {
                return Responses.dataResponse(Status.REJECTED, "Incorrect password!", null);
            }

            // TODO: JWT authentication
            return Responses.dataResponse(Status.SUCCESS, "Account found", toDTO(user.get()));
        } catch (Exception e) {
            System.err.println("An error has occurred while authorizing the user: " + e.getMessage());
            return Responses.dataResponse(Status.ERROR, "An error has occurred!", null);
        }
    }

    /**
     * Updates an existing user's data
     * @param id The user's primary key
     * @param updatedData The user's new data
     * @return A response entity confirming if the data was updated
     */
    public ResponseEntity<APIInfoResponseDTO> update(Long id, NewStaffDataDTO updatedData) {
        // Validate the arguments
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }
        if (updatedData == null) {
            return Responses.infoResponse(Status.REJECTED, "Provide the updated data!");
        }

        try {
            // Fetch the account
            Optional<Staff> staff = staffRepository.findById(id);

            // Check if user exists
            if (staff.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Account not found!");
            }

            // Update the user
            staffRepository.save(updateStaffData(staff.get(), updatedData));

            return Responses.infoResponse(Status.SUCCESS, "Account updated");
        } catch (DataIntegrityViolationException e) {
            // Get the cause of violation
            Throwable rootCause = e.getRootCause();
            String message = rootCause != null ? rootCause.getMessage() : e.getMessage();

            // Check for the specific field that has been violated
            if (message != null && message.contains("email_address")) {
                return Responses.infoResponse(Status.DUPLICATE, "Email address already in use!");
            } else if (message != null && message.contains("username")) {
                return Responses.infoResponse(Status.DUPLICATE, "Username already in use!");
            } else if (message != null && message.contains("phone_number")) {
                return Responses.infoResponse(Status.DUPLICATE, "Phone number already in use!");
            } else if (message != null && message.contains("national_id")) {
                return Responses.infoResponse(Status.DUPLICATE, "National ID exists!");
            }

            return Responses.infoResponse(Status.DUPLICATE, "An account with these credentials already exists!");
        } catch (Exception e) {
            System.err.println("An error has occurred while updating the account: " + e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Deletes a user record from the database
     * @param id The user's primary key
     * @return A Response Entity with a body confirming if deleted
     */
    public ResponseEntity<APIInfoResponseDTO> delete(Long id) {
        // Validate the id
        if (id == null || id <= 0) {
            return Responses.infoResponse(Status.REJECTED, "Provide a valid id!");
        }

        try {
            // Fetch the user's data
            Optional<Staff> staff = staffRepository.findById(id);

            // Check if user exists
            if (staff.isEmpty()) {
                return Responses.infoResponse(Status.NOT_FOUND, "Account not found!");
            }

            // Delete the user
            staffRepository.delete(staff.get());
            return Responses.infoResponse(Status.SUCCESS, "Account deleted");
        } catch (Exception e) {
            System.err.println("An error has occurred while deleting the account: " +e.getMessage());
            return Responses.infoResponse(Status.ERROR, "An error has occurred!");
        }
    }

    /**
     * Converts the staff object to a DTO
     * @param staff The user's data
     * @return A DTO that contains non-sensitive information
     */
    private StaffDataDTO toDTO(Staff staff) {
        return new StaffDataDTO(
                staff.getId(),
                staff.getFullName(),
                staff.getDateOfBirth().toString(),
                staff.getNationalId(),
                staff.getPhoneNumber(),
                staff.getEmailAddress(),
                staff.getAddress(),
                staff.getGender(),
                staff.getDepartment(),
                staff.getSpecialization(),
                staff.isActive(),
                staff.getLastLoginAt(),
                staff.getEmploymentDate()
        );
    }

    /**
     * Retrieves a user's data from their identifier
     * @param username The account username
     * @param email The user's email address
     * @param phone The user's phone number
     * @return An optional staff details
     */
    private Optional<Staff> getStaff(String username, String email, String phone) {
        Optional<User> user = Optional.empty();

        if (email != null) {
            user = userRepository.findByEmailAddress(email);
        } else if (phone != null) {
            user = userRepository.findByPhoneNumber(phone);
        } else if (username != null) {
            Optional<Staff> staff = staffRepository.findByUsername(username);
            if (staff.isPresent()) {
                user = Optional.of(staff.get());
            }
        }
        if (user.isPresent() && user.get() instanceof Staff staff) {
            return Optional.of(staff);
        }
        return Optional.empty();
    }

    /**
     * Helper function to update an object's data
     */
    private Staff updateStaffData(Staff oldData, NewStaffDataDTO updatedData) {
        oldData.setUsername(updatedData.username());
        oldData.setPassword(BCrypt.hashpw(updatedData.password(), BCrypt.gensalt(10)));
        oldData.setEmailAddress(updatedData.email());
        oldData.setPhoneNumber(updatedData.phone());
        oldData.setAddress(updatedData.address());
        oldData.setActive(updatedData.isActive());
        oldData.setDepartment(updatedData.department());
        oldData.setLastLoginAt(LocalDateTime.now());
        oldData.setRole(updatedData.role());
        oldData.setDateOfBirth(LocalDate.parse(updatedData.dob()));
        oldData.setGender(Gender.valueOf(updatedData.gender()));
        oldData.setSpecialization(updatedData.specialization());
        oldData.setFullName(updatedData.fullName());
        oldData.setNationalId(updatedData.nationalId());
        oldData.setUpdatedAt(LocalDateTime.now());
        return oldData;
    }
}
