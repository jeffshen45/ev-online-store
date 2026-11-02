package com.evstore.repository;

import com.evstore.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// DAO interface to handle database operations on our customers table
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // find a user by their email address during login
    Optional<Customer> findByEmail(String email);

    // check if an email already exists so we can reject duplicates on registration
    boolean existsByEmail(String email);

    // check if a username is already taken
    boolean existsByUsername(String username);
}

