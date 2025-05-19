package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;

public class LocationController {

    public static Response createLocation(String id, String name, String city,
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

            // Todo válido, pero aún no se implementa el almacenamiento
            return new Response("Location creation not implemented", Status.NOT_IMPLEMENTED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
