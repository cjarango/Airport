package core.controller;
import core.controller.utils.Response;
import core.controller.utils.Status;

public class PlaneController {

    public static Response createPlane(String id, String brand, String model,
            String maxCapacity, String airline) {
        try {
            // Validar ID: formato XXYYYYY (2 letras mayúsculas seguidas de 5 dígitos)
            if (id == null || !id.matches("^[A-Z]{2}\\d{5}$")) {
                return new Response("Plane ID must follow the format XXYYYYY (2 uppercase letters and 5 digits)", Status.BAD_REQUEST);
            }

            // Validar que brand, model y airline no estén vacíos
            if (brand == null || brand.trim().isEmpty()) {
                return new Response("Brand must not be empty", Status.BAD_REQUEST);
            }

            if (model == null || model.trim().isEmpty()) {
                return new Response("Model must not be empty", Status.BAD_REQUEST);
            }

            if (airline == null || airline.trim().isEmpty()) {
                return new Response("Airline must not be empty", Status.BAD_REQUEST);
            }

            // Validar y convertir maxCapacity
            int capacity;
            try {
                capacity = Integer.parseInt(maxCapacity);
                if (capacity <= 0) {
                    return new Response("Max capacity must be a positive number", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Max capacity must be numeric", Status.BAD_REQUEST);
            }

            // Si todo es válido, retornar 501 (aún no implementado el almacenamiento)
            return new Response("Plane creation not implemented", Status.NOT_IMPLEMENTED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

}
