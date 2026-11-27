package com.evstore.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.evstore.model.CartItem;
import com.evstore.model.Customer;
import com.evstore.model.PurchaseRecord;
import com.evstore.repository.PurchaseRecordRepository;
import com.evstore.service.CartService;
import com.evstore.service.CheckoutService;
import com.evstore.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
class CheckoutController {

    private static final BigDecimal LOAN_RATE = new BigDecimal("6.0");

    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final EmailService emailService;
    private final PurchaseRecordRepository purchaseRepo;

    CheckoutController(CartService cartService, CheckoutService checkoutService,
                       EmailService emailService, PurchaseRecordRepository purchaseRepo) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.emailService = emailService;
        this.purchaseRepo = purchaseRepo;
    }

    private Customer loggedIn(HttpSession session) {
        return (Customer) session.getAttribute("LOGGED_IN_CUSTOMER");
    }

    private String requireLogin(HttpSession session, String returnTo) {
        session.setAttribute("RETURN_TO", returnTo);
        return "redirect:/login";
    }

    @GetMapping("/checkout")
    String form(HttpSession session, Model model) {
        if (loggedIn(session) == null) {
            return requireLogin(session, "/checkout");
        }
        List<CartItem> items = cartService.getCart(session.getId());
        if (items.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.cartTotal(session.getId()));
        return "checkout";
    }

    @PostMapping("/checkout")
    String submit(@RequestParam String paymentType,
                  @RequestParam(defaultValue = "0") BigDecimal downPercent,
                  @RequestParam(defaultValue = "60") int months,
                  HttpSession session, RedirectAttributes redirect) {
        Customer customer = loggedIn(session);
        if (customer == null) {
            return requireLogin(session, "/checkout");
        }
        try {
            List<PurchaseRecord> records = checkoutService.checkout(
                    customer.getId(), session.getId(), paymentType, downPercent, months, LOAN_RATE);
            emailService.sendPurchaseConfirmation(customer, records);
            return "redirect:/purchases";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/purchases")
    String history(HttpSession session, Model model) {
        Customer customer = loggedIn(session);
        if (customer == null) {
            return requireLogin(session, "/purchases");
        }
        model.addAttribute("purchases",
                purchaseRepo.findByCustomer_IdOrderByPurchasedAtDesc(customer.getId()));
        return "purchases";
    }
}