package com.evstore.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.evstore.dto.VehicleFilter;
import com.evstore.model.Vehicle;
import com.evstore.model.VehicleOption;
import com.evstore.service.CartService;
import com.evstore.service.VehicleService;

@Controller
class VehicleViewController {

    private final VehicleService service;
    private final CartService cartService;

    VehicleViewController(VehicleService service, CartService cartService) {
        this.service = service;
        this.cartService = cartService;
    }

    @GetMapping("/vehicles")
    String list(
            @ModelAttribute("filter") VehicleFilter filter,
            Model model) {

        model.addAttribute(
                "vehicles",
                service.searchVehicles(filter));

        return "vehicles";
    }

    @GetMapping("/vehicles/new")
    String form(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle-form";
    }

    @PostMapping("/vehicles")
    String save(@ModelAttribute Vehicle vehicle) {
        service.save(vehicle);
        return "redirect:/vehicles";
    }

    @PostMapping("/vehicles/{id}/delete")
    String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/vehicles";
    }

    // Display individual vehicle details
    @GetMapping("/vehicles/{id}")
    String details(@PathVariable Long id, Model model) {

        Vehicle vehicle = service.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        Map<String, List<VehicleOption>> groups = cartService.getOptions(vehicle).stream()
                .collect(Collectors.groupingBy(
                        VehicleOption::getCategory, TreeMap::new, Collectors.toList()));

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("optionGroups", groups);
        return "vehicle-details";
    }
    
}
