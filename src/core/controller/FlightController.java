package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FlightController {

    public static Response createFlight(String id, String planeId, String departureLocation,
            String arrivalLocation, String departureDate, String hoursDurationArrival,
            String minutesDurationArrival) {
        try {
            // Validar formato de ID del vuelo: XXXYYY
            if (id == null || !id.matches("^[A-Z]{3}\\d{3}$")) {
                return new Response("Flight ID must follow the format XXXYYY (3 uppercase letters and 3 digits)", Status.BAD_REQUEST);
            }

            // Validar IDs del avión y de las localizaciones
            if (planeId == null || planeId.trim().isEmpty()) {
                return new Response("Plane ID must not be empty", Status.BAD_REQUEST);
            }

            if (departureLocation == null || departureLocation.trim().isEmpty()) {
                return new Response("Departure location ID must not be empty", Status.BAD_REQUEST);
            }

            if (arrivalLocation == null || arrivalLocation.trim().isEmpty()) {
                return new Response("Arrival location ID must not be empty", Status.BAD_REQUEST);
            }

            // Validar que departureDate sea una fecha válida (formato ISO_LOCAL_DATE_TIME)
            LocalDateTime parsedDate;
            try {
                parsedDate = LocalDateTime.parse(departureDate);
            } catch (DateTimeParseException e) {
                return new Response("Departure date must be a valid date in ISO format (yyyy-MM-ddTHH:mm)", Status.BAD_REQUEST);
            }

            // Validar duración (horas y minutos)
            int hours, minutes;
            try {
                hours = Integer.parseInt(hoursDurationArrival);
                minutes = Integer.parseInt(minutesDurationArrival);
                if (hours < 0 || minutes < 0 || (hours == 0 && minutes == 0)) {
                    return new Response("Flight duration must be greater than 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Flight duration must be numeric values", Status.BAD_REQUEST);
            }

            // Validación pendiente: existencia del avión y de las localizaciones
            // Aquí iría algo como:
            // Plane plane = Storage.getInstance().getPlaneById(planeId);
            // if (plane == null) return new Response("Plane not found", Status.BAD_REQUEST);
            // Igual para locations...
            // Si todo es válido
            return new Response("Flight creation not implemented", Status.NOT_IMPLEMENTED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
