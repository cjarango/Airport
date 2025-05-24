package core.controller.controllers;

import core.controller.observers.implementations.ObserverLocation;
import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.entity.Location;
import core.model.manager.implementations.ManagerLocation;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JTable;

public class LocationController {

    private final ManagerLocation managerLocation;
    private ObserverLocation observer;

    public LocationController(ManagerLocation managerLocation) {
        this.managerLocation = managerLocation;
        this.observer = null;
    }

    public void setObserver(JTable table) {
        if(this.observer == null){
            this.observer = ObserverLocation.getInstance(table);
        }
    }

    public Response createLocation(String id, String name, String city,
            String country, String latitude, String longitude) {
        try {
            // Validar ID: debe ser exactamente 3 letras mayúsculas
            if (id == null || !id.matches("^[A-Z]{3}$")) {
                return new Response("Airport ID must be exactly 3 uppercase letters", Status.BAD_REQUEST);
            }

            // Validar que name, city y country no estén vacíos
            if (name == null || name.trim().isEmpty()) {
                return new Response("Airport name must not be empty", Status.BAD_REQUEST);
            }

            if (city == null || city.trim().isEmpty()) {
                return new Response("Airport city must not be empty", Status.BAD_REQUEST);
            }

            if (country == null || country.trim().isEmpty()) {
                return new Response("Airport country must not be empty", Status.BAD_REQUEST);
            }

            // Validar y convertir latitud
            double lat;
            try {
                lat = Double.parseDouble(latitude);
                if (lat < -90 || lat > 90) {
                    return new Response("Latitude must be between -90 and 90", Status.BAD_REQUEST);
                }
                if (!latitude.matches("^-?\\d{1,2}(\\.\\d{1,4})?$")) {
                    return new Response("Latitude must have at most 4 decimal places", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Latitude must be numeric", Status.BAD_REQUEST);
            }

            // Validar y convertir longitud
            double lon;
            try {
                lon = Double.parseDouble(longitude);
                if (lon < -180 || lon > 180) {
                    return new Response("Longitude must be between -180 and 180", Status.BAD_REQUEST);
                }
                if (!longitude.matches("^-?\\d{1,3}(\\.\\d{1,4})?$")) {
                    return new Response("Longitude must have at most 4 decimal places", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Longitude must be numeric", Status.BAD_REQUEST);
            }

            Location newLocation = new Location(id, name, city, country, lat, lon);

            if (!managerLocation.add(newLocation)) {
                return new Response("An location with that id already exists", Status.BAD_REQUEST);
            }
            
            if(observer != null){
                observer.update(managerLocation.getAll());
            }
            
            
            return new Response("Location created successfully", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllLocations() {
        List<Location> originals = managerLocation.getAll();

        if (originals == null || originals.isEmpty()) {
            return new Response("No locations available", Status.OK, Collections.emptyList());
        }

        try {
            List<Location> copies = originals.stream()
                    .filter(Objects::nonNull)
                    .map(Location::clone)
                    .collect(Collectors.toList());
            return new Response("Locations retrieved successfully", Status.OK, copies);
        } catch (Exception e) {
            return new Response("Error cloning locations", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
