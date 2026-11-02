package com.evstore.controller;

import com.evstore.dto.LoginRequest;
import com.evstore.dto.RegistrationRequest;
import com.evstore.model.Customer;
import com.evstore.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// handles incoming HTTP web API requests for accounts and auth
@RestController
@RequestMapping("/api/auth")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // UC13 - Register Account: receives POST request with user details
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegistrationRequest req) {
        try {
            // pass request to business logic service
            Customer created = accountService.register(req);

            // prepare JSON success response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Registration successful.");
            responseBody.put("customerId", created.getId());
            responseBody.put("username", created.getUsername());

            //return HTTP 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        } catch (IllegalArgumentException ex) {
            // catches validation errors (duplicate email, empty fields, etc.)
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", ex.getMessage());

            // return HTTP 400 Bad Request
            return ResponseEntity.badRequest().body(errorBody);
        }
    }

    // UC14--> Sign In: verifies credentials and sets up session
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest req, HttpSession session) {
        try {
            // authenticate through account service
            Customer authenticatedCustomer = accountService.signIn(req);

            // save customer object into session state (like servlet session handling)
            session.setAttribute("LOGGED_IN_CUSTOMER", authenticatedCustomer);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful.");
            responseBody.put("username", authenticatedCustomer.getUsername());
            responseBody.put("email", authenticatedCustomer.getEmail());

            return ResponseEntity.ok(responseBody);

        } catch (IllegalArgumentException ex) {
            // invalid credentials or missing fields
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("error", ex.getMessage());

            // return HTTP 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody);
        }
    }

    // UC15 - Sign Out: destroys active session
    @PostMapping("/logout")
    public ResponseEntity<?> logoutCustomer(HttpSession session) {
        // invalidate current session completely
        session.invalidate();

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully.");
        return ResponseEntity.ok(responseBody);
    }

    // checks whether user currently has an active logged in session
    @GetMapping("/status")
    public ResponseEntity<?> checkSessionStatus(HttpSession session) {
        Customer currentCustomer = (Customer) session.getAttribute("LOGGED_IN_CUSTOMER");

        if (currentCustomer == null) {
            Map<String, Object> responseBody = new HashMap<>();
            
            responseBody.put("loggedIn", false);
            responseBody.put("message", "No active session.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("loggedIn", true);
        responseBody.put("username", currentCustomer.getUsername());
        responseBody.put("email", currentCustomer.getEmail());

        return ResponseEntity.ok(responseBody);
    }
}


