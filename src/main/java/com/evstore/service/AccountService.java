package com.evstore.service;

import com.evstore.dto.LoginRequest;
import com.evstore.dto.RegistrationRequest;
import com.evstore.model.Customer;
import com.evstore.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// service layer for managing customer accounts
@Service
public class AccountService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // handle user registration and basic validations
    public Customer register(RegistrationRequest request) {

        // check that required fields are not empty
        if (request.getUsername() == null || request.getUsername().trim().isEmpty() || request.getEmail() == null || request.getEmail().trim().isEmpty() ||request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Username, email, and password are all required.");
        }

        // check if email is already taken (Sequence Diagram 2 / TC-9 check)
        if (customerRepo.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        // check if username is already taken
        if (customerRepo.existsByUsername(request.getUsername().trim())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        // hash the plaintext password using BCrypt before storing
        String hashedPassword = encoder.encode(request.getPassword());

        // create customer object to persist
        Customer customerToSave = new Customer(request.getUsername().trim(), request.getEmail().trim(),hashedPassword,request.getAddress() != null ? request.getAddress().trim() : null);

        
        // store in the db and return the created record
        return customerRepo.save(customerToSave);
    }

    // handles user login verification (UC14)
    public Customer signIn(LoginRequest request) {

        // check that required fields are not empty
        if (request.getEmail() == null || request.getEmail().trim().isEmpty() ||request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Email and password are both required.");
        }

        // look up customer by email in the database
        Optional<Customer> foundCustomer = customerRepo.findByEmail(request.getEmail().trim());
        if (foundCustomer.isEmpty()) {
            // generic message to avoid leaking whether an account exists
            throw new IllegalArgumentException("Invalid email or password.");
        }

        Customer existingCustomer = foundCustomer.get();

        // check if raw password matches the stored BCrypt hash
        if (!encoder.matches(request.getPassword(), existingCustomer.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        // authentication passed, return customer object
        return existingCustomer;
    }
}



