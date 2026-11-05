package com.evstore.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.evstore.model.Vehicle;

@Component
class VehicleModelAssembler implements RepresentationModelAssembler<Vehicle, EntityModel<Vehicle>> {

  @Override
  public EntityModel<Vehicle> toModel(Vehicle vehicle) {
    return EntityModel.of(vehicle,
        linkTo(methodOn(VehicleController.class).one(vehicle.getId())).withSelfRel(),
        linkTo(methodOn(VehicleController.class).all("price", "asc")).withRel("vehicles"));
  }
}
