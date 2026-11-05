package com.evstore.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evstore.model.Vehicle;
import com.evstore.service.VehicleService;

@RestController
class VehicleController {

  private final VehicleService service;
  private final VehicleModelAssembler assembler;

  VehicleController(VehicleService service, VehicleModelAssembler assembler) {
    this.service = service;
    this.assembler = assembler;
  }

  //Sort by price and ascending as default
  @GetMapping("/api/vehicles")
  CollectionModel<EntityModel<Vehicle>> all(
	        @RequestParam(defaultValue = "price") String sort,
	        @RequestParam(defaultValue = "asc") String dir) {
	    List<EntityModel<Vehicle>> vehicles = service.getVehicles(sort, dir).stream()
	        .map(assembler::toModel)
	        .collect(Collectors.toList());

	    return CollectionModel.of(vehicles,
	        linkTo(methodOn(VehicleController.class).all(sort, dir)).withSelfRel());
	}

  @PostMapping("/api/vehicles")
  ResponseEntity<?> newVehicle(@RequestBody Vehicle newVehicle) {
    EntityModel<Vehicle> entityModel = assembler.toModel(service.save(newVehicle));

    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping("/api/vehicles/{id}")
  EntityModel<Vehicle> one(@PathVariable Long id) {
    Vehicle vehicle = service.findById(id)
        .orElseThrow(() -> new VehicleNotFoundException(id));

    return assembler.toModel(vehicle);
  }

  @PutMapping("/api/vehicles/{id}")
  ResponseEntity<?> replaceVehicle(@RequestBody Vehicle newVehicle, @PathVariable Long id) {
    Vehicle updated = service.findById(id)
        .map(vehicle -> {
          vehicle.setMake(newVehicle.getMake());
          vehicle.setModel(newVehicle.getModel());
          vehicle.setModelYear(newVehicle.getModelYear());
          vehicle.setPrice(newVehicle.getPrice());
          vehicle.setMileage(newVehicle.getMileage());
          return service.save(vehicle);
        })
        .orElseGet(() -> service.save(newVehicle));

    EntityModel<Vehicle> entityModel = assembler.toModel(updated);

    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @DeleteMapping("/api/vehicles/{id}")
  ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
