package com.evstore.dto;

// data transfer object to capture sign-in form input
public class LoginRequest {

    private String email;
    private String password;
    public LoginRequest() {
    	
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // getters and setters for Spring to map JSON fields
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
    
}

