package com.softcafe.clinic_system.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

public class Util {

    /**
     * Checks if the string passed is an email
     * @param email The suspected email string
     * @return true if an email, false otherwise
     */
    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (email == null) return false;
        return emailPattern.matcher(email).matches();
    }

    /**
     * Checks if the string provided is a phone number
     * @param phone The suspected phone number string
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        Pattern phonePattern = Pattern.compile("^\\+?[0-9]{10,15}$");
        if (phone == null) return false;
        return phonePattern.matcher(phone).matches();
    }

    /**
     * Changes the casing of a sentence to a Word Case
     * @param sentence Sentence string
     * @return A Word Cased string
     */
    public static String wordCase(String sentence) {
        if (sentence == null || sentence.isBlank()) {
            return sentence;
        }

        StringBuilder result = new StringBuilder();

        for (String word : sentence.strip().split("\\s+")) {
            if (!word.isEmpty()) {
                String newWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                result.append(newWord).append(" ");
            }
        }

        return result.toString().strip();
    }

    /**
     * Checks what kind of violation has occurred
     * @param e The data integrity violation exception
     * @return A string with the violated field or null
     */
    public static String parseViolation(DataIntegrityViolationException e) {
        String message = e.getMessage().toLowerCase();

        if (message.contains("email_address")) {
            return "email";
        }
        if (message.contains("phone_number")) {
            return "phone number";
        }
        if (message.contains("national_id_number")) {
            return "ID number";
        }
        if (message.contains("insurance_number")) {
            return "insurance number";
        }

        return null;
    }

    /**
     * Checks if the ID is provided and is correct
     * @param id The Primary Key
     * @throws ResponseStatusException BAD_REQUEST In case of invalid ID
     */
    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a valid ID!");
        }
    }
}
