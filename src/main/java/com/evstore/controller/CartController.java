package com.evstore.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.evstore.service.CartService;

@Controller
class CartController {

    private static final Long CUSTOMER_ID = 1L;   // TODO: real customer at checkout

    private final CartService cartService;

    CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add")
    String add(@RequestParam Long vehicleId, @RequestParam Map<String, String> params) {
        Map<String, String> choices = new HashMap<>();
        params.forEach((k, v) -> {
            if (k.startsWith("opt_") && !v.isBlank()) {
                choices.put(k.substring(4), v);
            }
        });
        cartService.addToCart(CUSTOMER_ID, vehicleId, choices);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    String view(Model model) {
        model.addAttribute("items", cartService.getCart(CUSTOMER_ID));
        model.addAttribute("total", cartService.cartTotal(CUSTOMER_ID));
        return "cart";
    }

    @PostMapping("/cart/{id}/remove")
    String remove(@PathVariable Long id) {
        cartService.remove(id);
        return "redirect:/cart";
    }
}