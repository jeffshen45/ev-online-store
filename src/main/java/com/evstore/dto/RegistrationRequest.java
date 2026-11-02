package com.evstore.dto;

// data transfer object to hold form input when someone signs up
public class RegistrationRequest {

    private String username;
    private String email;
    private String password;
    private String address;

    public RegistrationRequest() {}

    public RegistrationRequest(String username, String email, String password, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    // getters and setters for Spring to map JSON fields automatically
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}

