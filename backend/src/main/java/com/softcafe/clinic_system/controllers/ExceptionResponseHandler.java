package com.softcafe.clinic_system.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseEntity<Map<String,String>> responseStatusExceptionHandler(ResponseStatusException ex) {
        Map<String,String> body = new HashMap<>();
        body.put("message", ex.getReason()); // The custom message you threw
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}
