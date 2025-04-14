package com.softcafe.local_clinic.Services;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Responses {
    /**
     * Creates a Spring Boot Response Entity with an object body
     * @param status The response's success
     * @param message A string information about the process result
     * @return A response entity containing a map of String to String data type containing the status and message
     */
    public static ResponseEntity<Map<String, String>> infoResponse(Status status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("status", status.toString());
        response.put("message", message);
        return ResponseEntity.status(getStatusCode(status)).body(response);
    }

    /**
     * Creates a spring boot response entity with a mapped object body
     * @param status The response's success
     * @param message A string information about the process result
     * @param data Any requested data
     * @return A spring boot response entity containing a map of the status, message and data as the body
     */
    public static ResponseEntity<Map<String, Object>> dataResponse(Status status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.toString());
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.status(getStatusCode(status)).body(response);
    }

    /**
     * Checks for the appropriate response code
     * @param status The status of a response
     * @return The HTTP code
     */
    private static int getStatusCode (Status status) {
        return switch (status) {
            case CREATED -> 201;
            case SUCCESS -> 200;
            case REJECTED -> 400;
            case DUPLICATE -> 409;
            case NOT_FOUND -> 404;
            case ERROR -> 500;
        };
    }
}
