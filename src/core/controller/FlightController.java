package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.Flight;
import core.model.Location;
import core.model.Plane;
import core.model.storage.StorageFlight;
import core.model.storage.StorageLocation;
import core.model.storage.StoragePlane;
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

            if (!planeId.matches("^[A-Z]{2}\\d{5}$")) {
                return new Response("Invalid plane ID format. Must be: 2 uppercase letters followed by 5 digits (Example: AB12345)", Status.BAD_REQUEST);
            }

            // buscamos el id del avión 
            Plane plane = StoragePlane.getInstance().getById(planeId);

            if (plane == null) {
                return new Response("Plane not found", Status.NOT_FOUND);
            }

            if (departureLocation == null || departureLocation.trim().isEmpty()) {
                return new Response("Departure location ID must not be empty", Status.BAD_REQUEST);
            }

            if (!departureLocation.matches("^[A-Z]{3}$")) {
                return new Response("Airport ID must be exactly 3 uppercase letters", Status.BAD_REQUEST);
            }

            Location departure = StorageLocation.getInstance().getById(departureLocation);

            if (departure == null) {
                return new Response("Departure location not found", Status.NOT_FOUND);
            }

            if (arrivalLocation == null || arrivalLocation.trim().isEmpty()) {
                return new Response("Arrival location ID must not be empty", Status.BAD_REQUEST);
            }

            // validamos las localizaciones
            if (!arrivalLocation.matches("^[A-Z]{3}$")) {
                return new Response("Airport ID must be exactly 3 uppercase letters", Status.BAD_REQUEST);
            }

            Location arrival = StorageLocation.getInstance().getById(arrivalLocation);

            if (arrival == null) {
                return new Response("Arrival location not found", Status.NOT_FOUND);
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

            Flight newFlight = new Flight(id, plane, departure, arrival, parsedDate, hours, minutes);

            if (!StorageFlight.getInstance().add(newFlight)) {
                return new Response("An flight with that id already exists", Status.BAD_REQUEST);
            }
            return new Response("Flight created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
