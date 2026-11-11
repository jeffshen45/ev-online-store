package com.evstore.controller;

import com.evstore.dto.LoginRequest;
import com.evstore.dto.RegistrationRequest;
import com.evstore.dto.TestDriveRequest;
import com.evstore.model.Customer;
import com.evstore.service.AccountService;
import com.evstore.service.TestDriveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountViewController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TestDriveService testDriveService;

    //register Page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(RegistrationRequest req, Model model) {
        try {
            accountService.register(req);
            model.addAttribute("success", "Registration successful! Please sign in.");
            return "login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    // Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    
    @PostMapping("/login")
    public String processLogin(LoginRequest req, HttpSession session, Model model) {
        try {
            Customer authenticated = accountService.signIn(req);
            session.setAttribute("LOGGED_IN_CUSTOMER", authenticated);
            return "redirect:/testdrive";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    // logout
    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // test Drive Page 
    @GetMapping("/testdrive")
    public String showTestDrivePage(HttpSession session, Model model) {
        Customer current = (Customer) session.getAttribute("LOGGED_IN_CUSTOMER");
        if (current == null) {
            return "redirect:/login";
        }
        model.addAttribute("customer", current);
        return "testdrive";
    }

    
    
    @PostMapping("/testdrive")
    public String processTestDrive(TestDriveRequest req, HttpSession session, Model model) {
        Customer current = (Customer) session.getAttribute("LOGGED_IN_CUSTOMER");
        if (current == null) {
            return "redirect:/login";
        }

        try {
            testDriveService.scheduleBooking(current.getId(), req);
            model.addAttribute("customer", current);
            
            model.addAttribute("success", "Test drive scheduled successfully!");
            return "testdrive";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("customer", current);
            model.addAttribute("error", ex.getMessage());
            return "testdrive";
        }
    }
}


