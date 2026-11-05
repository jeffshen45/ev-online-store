package com.evstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evstore.model.Vehicle;
import com.evstore.service.VehicleService;

@Controller
class VehicleViewController {

  private final VehicleService service;

  VehicleViewController(VehicleService service) {
    this.service = service;
  }

  @GetMapping("/vehicles")
  String list(@RequestParam(defaultValue = "price") String sort,
          @RequestParam(defaultValue = "asc") String dir,
          Model model) {
	  model.addAttribute("vehicles", service.getVehicles(sort, dir));
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
}