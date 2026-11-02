package com.evstore.model;

import jakarta.persistence.*;

// maps this class to our "customers" table in the database
@Entity
@Table(name = "customers")
public class Customer {

    // primary key, auto increments with each new customer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username must be filled out and unique
    @Column(nullable = false, unique = true)
    private String username;

    // email must be unique so duplicate accounts aren't made
    @Column(nullable = false, unique = true)
    private String email;

   
    @Column(nullable = false)     // store the hashed password here, never raw text
    private String password;    

    // optional address field, needed for at home test drive bookings
    private String address;

    
    
    public Customer() {}

    public Customer(String username, String email, String password, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    // getters and setters 
    public Long getId() {
    	return id; 
    }
    public void setId(Long id) {
    	this.id = id; 
    }

    public String getUsername() {
    	return username; 
    }
    public void setUsername(String username) {
    	this.username = username; 
    }

    public String getEmail() {
    	return email; 
    }
    public void setEmail(String email) {
    	this.email = email;
    }

    public String getPassword() {
    	return password; 
    }
    public void setPassword(String password) {
    	this.password = password; 
    }

    public String getAddress() {
    	return address; 
    }
    public void setAddress(String address) {
    	this.address = address; 
    }
}

