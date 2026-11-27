package com.evstore.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.evstore.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
class CartController {

    private final CartService cartService;

    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add")
    String add(@RequestParam Long vehicleId, @RequestParam Map<String, String> params,
               HttpSession session) {
        Map<String, String> choices = new HashMap<>();
        params.forEach((k, v) -> {
            if (k.startsWith("opt_") && !v.isBlank()) {
                choices.put(k.substring(4), v);
            }
        });
        cartService.addToCart(session.getId(), vehicleId, choices);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    String view(Model model, HttpSession session) {
        model.addAttribute("items", cartService.getCart(session.getId()));
        model.addAttribute("total", cartService.cartTotal(session.getId()));
        return "cart";
    }

    @PostMapping("/cart/{id}/remove")
    String remove(@PathVariable Long id, HttpSession session) {
        cartService.remove(session.getId(), id);
        return "redirect:/cart";
    }
}