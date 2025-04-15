package com.softcafe.local_clinic.Services;

import com.softcafe.local_clinic.DTO.APIResponse.APIDataResponseDTO;
import com.softcafe.local_clinic.DTO.APIResponse.APIInfoResponseDTO;
import org.springframework.http.ResponseEntity;

public class Responses {
    /**
     * Creates a Spring Boot Response Entity with an object body
     * @param status The response's success
     * @param message A string information about the process result
     * @return A response entity containing an APIInfoResponseDTO as the body
     */
    public static ResponseEntity<APIInfoResponseDTO> infoResponse(Status status, String message) {
        return ResponseEntity.status(getStatusCode(status)).body(new APIInfoResponseDTO(status, message));
    }

    /**
     * Creates a spring boot response entity with a mapped object body
     * @param status The response's success
     * @param message A string information about the process result
     * @param data Any requested data
     * @return A spring boot response entity containing an APIDataResponseDTO as the body
     */
    public static ResponseEntity<APIDataResponseDTO> dataResponse(Status status, String message, Object data) {
        return ResponseEntity.status(getStatusCode(status)).body(new APIDataResponseDTO(status, message, data));
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
