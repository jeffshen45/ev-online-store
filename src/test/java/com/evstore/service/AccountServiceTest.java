package com.evstore.service;

import com.evstore.dto.LoginRequest;
import com.evstore.dto.RegistrationRequest;
import com.evstore.model.Customer;
import com.evstore.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private CustomerRepository customerRepo;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AccountService accountService;

    // test fixtures
    private RegistrationRequest validRegistrationRequest;
    private LoginRequest validLoginRequest;
    private Customer existingDatabaseCustomer;


    @BeforeEach
    void setUpTestData() {
        validRegistrationRequest = new RegistrationRequest();
        validRegistrationRequest.setUsername("testuser10");
        validRegistrationRequest.setEmail("testuser10@example.com");
        validRegistrationRequest.setPassword("ValidPassword123!");
        validRegistrationRequest.setAddress("123 Test Street, Mississauga");

        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("testuser10@example.com");
        validLoginRequest.setPassword("ValidPassword123!");

        // mock customer returned from db
        existingDatabaseCustomer = new Customer("testuser10", "testuser10@example.com", "$2a$10$hashedPasswordValueString", "123 Test Street, Mississauga");
    }



    @Test
    @DisplayName("Should successfully register customer when all details are valid and unique")
    void shouldRegisterNewCustomerSuccessfully() {
        when(customerRepo.existsByEmail("testuser10@example.com")).thenReturn(false);
        when(customerRepo.existsByUsername("testuser10")).thenReturn(false);
        when(encoder.encode("ValidPassword123!")).thenReturn("$2a$10$hashedPasswordValueString");
        when(customerRepo.save(any(Customer.class))).thenReturn(existingDatabaseCustomer);

        Customer registeredCustomerResult = accountService.register(validRegistrationRequest);

        assertNotNull(registeredCustomerResult);
        assertEquals("testuser10", registeredCustomerResult.getUsername());
        assertEquals("testuser10@example.com", registeredCustomerResult.getEmail());
        verify(customerRepo, times(1)).save(any(Customer.class));
    }


    @Test
    @DisplayName("Should throw exception when registering with missing or blank required fields")
    void shouldFailRegistrationWhenRequiredFieldsAreMissing() {
        RegistrationRequest emptyRegistrationRequest = new RegistrationRequest();
        emptyRegistrationRequest.setUsername("");
        emptyRegistrationRequest.setEmail("");
        emptyRegistrationRequest.setPassword("");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> accountService.register(emptyRegistrationRequest));

        assertEquals("Username, email, and password are all required.", thrownException.getMessage());
        verify(customerRepo, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw exception when registering with an email that already exists")
    void shouldFailRegistrationWhenEmailAlreadyExists() {
        when(customerRepo.existsByEmail("testuser10@example.com")).thenReturn(true);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> accountService.register(validRegistrationRequest));

        assertEquals("Email is already registered.", thrownException.getMessage());
        verify(customerRepo, never()).save(any(Customer.class));
    }


    @Test
    @DisplayName("Should throw exception when registering with a username that already exists")
    void shouldFailRegistrationWhenUsernameAlreadyExists() {
        when(customerRepo.existsByEmail("testuser10@example.com")).thenReturn(false);
        when(customerRepo.existsByUsername("testuser10")).thenReturn(true);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> accountService.register(validRegistrationRequest));

        assertEquals("Username is already taken.", thrownException.getMessage());
        verify(customerRepo, never()).save(any(Customer.class));
    }



    // Sign In Tests:

    @Test
    @DisplayName("Should successfully sign in when email and password match database records")
    void shouldSignInSuccessfullyWithValidCredentials() {
        when(customerRepo.findByEmail("testuser10@example.com")).thenReturn(Optional.of(existingDatabaseCustomer));
        when(encoder.matches("ValidPassword123!", existingDatabaseCustomer.getPassword())).thenReturn(true);

        Customer signedInCustomerResult = accountService.signIn(validLoginRequest);

        assertNotNull(signedInCustomerResult);
        assertEquals("testuser10@example.com", signedInCustomerResult.getEmail());
    }

    @Test
    @DisplayName("Should throw exception when sign in is attempted with missing email or password")
    void shouldFailSignInWhenRequiredFieldsAreMissing() {
        LoginRequest emptyLoginRequest = new LoginRequest();
        emptyLoginRequest.setEmail("");
        emptyLoginRequest.setPassword("");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> accountService.signIn(emptyLoginRequest));

        assertEquals("Email and password are both required.", thrownException.getMessage());
    }


    @Test
    @DisplayName("Should throw exception when email is not found during sign in")
    void shouldFailSignInWhenEmailDoesNotExist() {
        when(customerRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        LoginRequest unknownEmailLoginRequest = new LoginRequest();
        unknownEmailLoginRequest.setEmail("nonexistent@example.com");
        unknownEmailLoginRequest.setPassword("ValidPassword123!");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> accountService.signIn(unknownEmailLoginRequest));

        assertEquals("Invalid email or password.", thrownException.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when password does not match stored hash during sign in")
    void shouldFailSignInWhenPasswordIsIncorrect() {
        when(customerRepo.findByEmail("testuser10@example.com")).thenReturn(Optional.of(existingDatabaseCustomer));
        when(encoder.matches("WrongPassword123!", existingDatabaseCustomer.getPassword())).thenReturn(false);

        LoginRequest wrongPasswordLoginRequest = new LoginRequest();
        wrongPasswordLoginRequest.setEmail("testuser10@example.com");
        wrongPasswordLoginRequest.setPassword("WrongPassword123!");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> accountService.signIn(wrongPasswordLoginRequest));

        assertEquals("Invalid email or password.", thrownException.getMessage());
    }

}



