package com.softcafe.clinic_system.utils;

import com.softcafe.clinic_system.dto.staff.NewStaff;
import com.softcafe.clinic_system.dto.staff.StaffData;
import com.softcafe.clinic_system.entities.Staff;
import com.softcafe.clinic_system.entities.StaffStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class StaffUtil {
    /**
     * validates new staff data to match database requirements
     * @param dto The new staff data
     * @throws IllegalArgumentException In case of invalid data
     */
    public static void validate(NewStaff dto) {
        validater(dto);

        if (dto.password() == null) {
            throw new IllegalArgumentException("Provide the staff's password!");
        }

        if (dto.role() == null) {
            throw new IllegalArgumentException("Provide the staff's role!");
        }

    }

    /**
     * Converts a DTO to a system object
     * @param dto The staff's DTO
     * @return A system object
     */
    public static Staff toStaff(NewStaff dto) {
        Staff staff = new Staff();
        staff.setFullName(Util.wordCase(dto.fullName().trim()));
        staff.setAddress(dto.address());
        staff.setCreatedAt(LocalDateTime.now());
        staff.setDateOfBirth(dto.dateOfBirth());
        staff.setEmail(dto.email().toLowerCase().trim());
        staff.setGender(dto.gender());
        staff.setNationalId(dto.nationalId().trim());
        staff.setPhone(dto.phone().trim());
        staff.setUpdatedAt(LocalDateTime.now());
        if (dto.image() != null)
            staff.setImage(dto.image().getName().trim());
        staff.setUsername(dto.username().toLowerCase());
        staff.setPassword(dto.password());
        staff.setRole(dto.role());
        staff.setLastLogin(LocalDateTime.now());
        staff.setStatus(StaffStatus.OFF);
        return staff;
    }

    /**
     * Converts a database object to the staff DTO
     * @param staff Staff object
     * @return Staff DTO
     */
    public static StaffData toDto(Staff staff) {
        return new StaffData(
                staff.getId(),
                staff.getFullName(),
                staff.getUsername(), 
                staff.getEmail(),
                staff.getPhone(),
                staff.getNationalId(),
                staff.getAddress(),
                staff.getDateOfBirth(),
                staff.getGender(),
                staff.getImage(),
                staff.getStatus(),
                staff.getRole(),
                staff.getLastLogin(),
                staff.getCreatedAt(),
                staff.getUpdatedAt()
        );
    }

    /**
     * Updates the staff object's data to the DTO's data
     * @param oldData Staff object data
     * @param newData DTO data
     */
    public static void update(Staff oldData, NewStaff newData) {
        if (!oldData.getEmail().equalsIgnoreCase(newData.email().trim())) {
            oldData.setEmail(newData.email().toLowerCase().trim());
        }

        if (!oldData.getPhone().equals(newData.phone().trim())) {
            oldData.setPhone(newData.phone().trim());
        }

        if (!oldData.getUsername().equalsIgnoreCase(newData.username())) {
            oldData.setUsername(newData.username().toLowerCase());
        }

        if (!oldData.getNationalId().equals(newData.nationalId().trim())) {
            oldData.setNationalId(newData.nationalId().trim());
        }

        if (newData.password() != null && !newData.password().isBlank()) {
            oldData.setPassword(newData.password());
        }

        oldData.setFullName(Util.wordCase(newData.fullName().trim()));
        oldData.setAddress(newData.address());
        oldData.setDateOfBirth(newData.dateOfBirth());
        oldData.setGender(newData.gender());
        oldData.setRole(newData.role());
        oldData.setStatus(newData.staffStatus());

        if (newData.image() != null)
            oldData.setImage(newData.image().getName().trim());
    }


    public static void validateUpdatedData(NewStaff dto) {
        validater(dto);

        if (dto.role() == null) {
            throw new IllegalArgumentException("Provide the staff's role!");
        }
    }

    private static void validater(NewStaff dto) {
        if (dto.fullName() == null) {
            throw new IllegalArgumentException("Provide patient's full name!");
        }

        if (dto.address() == null) {
            throw new IllegalArgumentException("Provide patient's location address!");
        }

        if (dto.phone() == null) {
            throw new IllegalArgumentException("Provide patient's phone number!");
        } else {
            if (!Util.isValidPhone(dto.phone())) {
                throw new IllegalArgumentException("Provide a valid phone number!");
            }
        }

        if (dto.email() != null) {
            if (!Util.isValidEmail(dto.email())) {
                throw new IllegalArgumentException("Provide a valid email address!");
            }
        }

        if (dto.nationalId() == null) {
            throw new IllegalArgumentException("Provide a national ID card number!");
        }

        if (dto.dateOfBirth() == null) {
            throw new IllegalArgumentException("Provide the date of birth!");
        }

        if (dto.gender() == null) {
            throw new IllegalArgumentException("Provide the patient's gender!");
        } else {
            if (!Objects.equals(String.valueOf(dto.gender()), "MALE") && !Objects.equals(String.valueOf(dto.gender()), "FEMALE")) {
                throw new IllegalArgumentException("Provide a valid gender!");
            }
        }

        if (dto.username() == null) {
            throw new IllegalArgumentException("Provide the staff's username!");
        }
    }
}
