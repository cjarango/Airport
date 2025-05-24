package core.controller.controllers;

import core.controller.observers.implementations.ObserverPlane;
import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.entity.Plane;
import core.model.manager.implementations.ManagerPlane;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JTable;

public class PlaneController {
    
    private final ManagerPlane managerPlane;
    private ObserverPlane observer;

    public PlaneController(ManagerPlane managerPlane) {
        this.managerPlane = managerPlane;
        this.observer = null;
    }

    public void setObserver(JTable table) {
        if(this.observer == null){
            this.observer = ObserverPlane.getInstance(table);
        }
    }

    public Response createPlane(String id, String brand, String model,
            String maxCapacity, String airline) {
        try {
            // Validación de parámetros
            Response validation = validatePlaneParameters(id, brand, model, maxCapacity, airline);
            if (validation != null) {
                return validation;
            }

            int capacity = Integer.parseInt(maxCapacity);
            Plane newPlane = new Plane(id, brand, model, capacity, airline);

            if (!managerPlane.add(newPlane)) {
                return new Response("Plane with ID " + id + " already exists", Status.BAD_REQUEST);
            }
            
            if(observer != null){
                observer.update(managerPlane.getAll());
            }
            
            return new Response("Plane created successfully", Status.CREATED, newPlane);

        } catch (Exception e) {
            return new Response("Unexpected error: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllPlanes() {
        try {
            List<Plane> planes = managerPlane.getAll();

            if (planes == null || planes.isEmpty()) {
                return new Response("No planes available", Status.OK, Collections.emptyList());
            }

            List<Plane> copies = planes.stream()
                    .filter(Objects::nonNull)
                    .map(Plane::clone)
                    .collect(Collectors.toList());

            return new Response("Planes retrieved successfully", Status.OK, copies);
        } catch (Exception e) {
            return new Response("Error retrieving planes: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static Response validatePlaneParameters(String id, String brand, String model,
            String maxCapacity, String airline) {
        if (id == null || !id.matches("^[A-Z]{2}\\d{5}$")) {
            return new Response("Invalid plane ID format (XXYYYYY)", Status.BAD_REQUEST);
        }

        if (brand == null || brand.trim().isEmpty()) {
            return new Response("Brand is required", Status.BAD_REQUEST);
        }

        if (model == null || model.trim().isEmpty()) {
            return new Response("Model is required", Status.BAD_REQUEST);
        }

        if (airline == null || airline.trim().isEmpty()) {
            return new Response("Airline is required", Status.BAD_REQUEST);
        }

        try {
            int capacity = Integer.parseInt(maxCapacity);
            if (capacity <= 0) {
                return new Response("Capacity must be positive", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("Invalid capacity format", Status.BAD_REQUEST);
        }

        return null;
    }
}
