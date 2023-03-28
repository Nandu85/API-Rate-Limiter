package com.narola.apiratelimiterdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {

    @GetMapping("/hello")
    public ResponseEntity<String> getResponse() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("Hello world!!!");
    }

    @GetMapping(value = "/users/data",produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDataForUser(@RequestHeader("username") String username) {
        return "Hello " + username;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Map<String, String> handleUserNotFoundException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", ex.getMessage());
        return errorResponse;
    }


}
