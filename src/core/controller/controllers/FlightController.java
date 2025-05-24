package core.controller.controllers;

import core.controller.observers.implementations.ObserverFlight;
import core.controller.observers.implementations.ObserverPassenger;
import core.controller.observers.implementations.ObserverPassengerFlight;
import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.entity.Flight;
import core.model.entity.Location;
import core.model.entity.Passenger;
import core.model.entity.Plane;
import core.model.manager.implementations.ManagerFlight;
import core.model.manager.implementations.ManagerLocation;
import core.model.manager.implementations.ManagerPassenger;
import core.model.manager.implementations.ManagerPlane;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JTable;

public class FlightController {

    // Instancias de storages y managers
    private final ManagerFlight managerFlight;
    private final ManagerPlane managerPlane;
    private final ManagerLocation managerLocation;
    private final ManagerPassenger managerPassenger;

    private ObserverFlight observer;
    private ObserverPassenger observerPassenger;
    private ObserverPassengerFlight observerPassengerFlight;

    public FlightController(
            ManagerFlight managerFlight,
            ManagerPlane managerPlane,
            ManagerLocation managerLocation,
            ManagerPassenger managerPassenger
    ) {
        this.managerFlight = managerFlight;
        this.managerPlane = managerPlane;
        this.managerLocation = managerLocation;
        this.managerPassenger = managerPassenger;
        this.observer = null;
        this.observerPassenger = null;
        this.observerPassengerFlight = null;
    }

    public void setObserver(JTable table) {
        if (this.observer == null) {
            this.observer = ObserverFlight.getInstance(table);
        }
    }

    public void setObserverPassenger(JTable table) {
        if (this.observerPassenger == null) {
            this.observerPassenger = ObserverPassenger.getInstance(table);
        }
    }

    public void setObserverPassengerFlight(JTable table) {
        if (this.observerPassengerFlight == null) {
            this.observerPassengerFlight = ObserverPassengerFlight.getInstance(table);
        }
    }

    public Response createFlight(String id, String planeId, String departureLocation,
            String arrivalLocation, String departureDate, String hoursDurationArrival,
            String minutesDurationArrival, String scaleLocation,
            String hoursDurationScale, String minutesDurationScale) {

        try {
            // Validar ID de vuelo
            if (id == null || !id.matches("^[A-Z]{3}\\d{3}$")) {
                return new Response("Flight ID must follow the format XXXYYY (3 uppercase letters and 3 digits)", Status.BAD_REQUEST);
            }

            // Validar avión
            if (planeId == null || planeId.trim().isEmpty()) {
                return new Response("Plane ID must not be empty", Status.BAD_REQUEST);
            }

            if (!planeId.matches("^[A-Z]{2}\\d{5}$")) {
                return new Response("Invalid plane ID format. Must be: 2 uppercase letters followed by 5 digits (Example: AB12345)", Status.BAD_REQUEST);
            }

            Plane plane = managerPlane.getById(planeId); // Obtener el avión mediante el manager
            if (plane == null) {
                return new Response("Plane not found", Status.NOT_FOUND);
            }

            // Validar localización de salida
            if (departureLocation == null || !departureLocation.matches("^[A-Z]{3}$")) {
                return new Response("Invalid departure location ID. Must be 3 uppercase letters", Status.BAD_REQUEST);
            }

            Location departure = managerLocation.getById(departureLocation); // Obtener locación de salida
            if (departure == null) {
                return new Response("Departure location not found", Status.NOT_FOUND);
            }

            // Validar localización de llegada
            if (arrivalLocation == null || !arrivalLocation.matches("^[A-Z]{3}$")) {
                return new Response("Invalid arrival location ID. Must be 3 uppercase letters", Status.BAD_REQUEST);
            }

            Location arrival = managerLocation.getById(arrivalLocation);
            if (arrival == null) {
                return new Response("Arrival location not found", Status.NOT_FOUND);
            }

            // Validar fecha
            LocalDateTime parsedDate;
            try {
                parsedDate = LocalDateTime.parse(departureDate);
            } catch (DateTimeParseException e) {
                return new Response("Departure date must be a valid ISO date (yyyy-MM-ddTHH:mm)", Status.BAD_REQUEST);
            }

            // Validar duración de llegada
            int hoursArrival, minutesArrival;
            try {
                hoursArrival = Integer.parseInt(hoursDurationArrival);
                minutesArrival = Integer.parseInt(minutesDurationArrival);
                if (hoursArrival < 0 || minutesArrival < 0 || (hoursArrival == 0 && minutesArrival == 0)) {
                    return new Response("Flight arrival duration must be greater than 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Arrival duration must be numeric", Status.BAD_REQUEST);
            }

            // Validar escala: si no se proporciona, se usa null y duración 0
            Location scale = null;
            int hoursScale = 0;
            int minutesScale = 0;

            if (scaleLocation != null && !scaleLocation.trim().isEmpty()) {
                if (!scaleLocation.matches("^[A-Z]{3}$")) {
                    return new Response("Scale location ID must be 3 uppercase letters", Status.BAD_REQUEST);
                }

                scale = managerLocation.getById(scaleLocation);
                if (scale == null) {
                    return new Response("Scale location not found", Status.NOT_FOUND);
                }

                try {
                    hoursScale = Integer.parseInt(hoursDurationScale);
                    minutesScale = Integer.parseInt(minutesDurationScale);
                    if (hoursScale < 0 || minutesScale < 0) {
                        return new Response("Scale duration must not be negative", Status.BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    return new Response("Scale duration must be numeric", Status.BAD_REQUEST);
                }
            } else {
                // Si no hay escala, verificar que se haya mandado "0"
                if (!"0".equals(hoursDurationScale) || !"0".equals(minutesDurationScale)) {
                    return new Response("If no scale is provided, scale duration must be 00:00", Status.BAD_REQUEST);
                }
            }

            // Crear el vuelo usando SIEMPRE el mismo constructor
            Flight flight = new Flight(id, plane, departure, scale, arrival, parsedDate,
                    hoursArrival, minutesArrival, hoursScale, minutesScale);

            if (!managerFlight.add(flight)) {
                return new Response("A flight with that ID already exists", Status.BAD_REQUEST);
            }

            if (observer != null) {
                observer.update(managerFlight.getAll());
            }

            return new Response("Flight created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response addPassenger(String passengerId, String flightId) {
        try {
            // Validar ID del pasajero
            long idLong;
            try {
                idLong = Long.parseLong(passengerId);
                if (idLong < 0 || passengerId.length() > 15) {
                    return new Response("Passenger ID must be >= 0 and at most 15 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Passenger ID must be numeric", Status.BAD_REQUEST);
            }

            // Buscar al pasajero
            Passenger passenger = managerPassenger.getById(idLong);
            if (passenger == null) {
                return new Response("Passenger not found", Status.NOT_FOUND);
            }

            // Validar ID del vuelo (formato XXXYYY)
            if (flightId == null || !flightId.matches("^[A-Z]{3}\\d{3}$")) {
                return new Response("Flight ID must follow the format XXXYYY (3 uppercase letters and 3 digits)", Status.BAD_REQUEST);
            }

            // Buscar vuelo
            Flight flight = managerFlight.getById(flightId);
            if (flight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }

            // Validar capacidad
            if (flight.getNumPassengers() >= flight.getPlane().getMaxCapacity()) {
                return new Response("Flight is full", Status.BAD_REQUEST);
            }

            // Agregar pasajero
            if (!managerFlight.addPassenger(flight, passenger)) {
                return new Response("Passenger is already in flight " + flightId, Status.BAD_REQUEST);
            }

            // Notificar observadores
            if (observer != null) {
                observer.update(managerFlight.getAll());
            }
            if (observerPassenger != null) {
                observerPassenger.update(managerPassenger.getAll());
            }
            if (observerPassengerFlight != null) {
                observerPassengerFlight.update(passenger.getFlights());
            }

            return new Response("Passenger added to flight successfully", Status.OK);
        } catch (Exception e) {
            e.printStackTrace(); // o usar un logger
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delays the departure time of a specific flight by the given number of
     * hours and minutes.
     * <p>
     * This method first validates the flight ID format (must match XXXYYY: 3
     * uppercase letters followed by 3 digits), checks if the flight exists in
     * storage, and validates that the delay is a positive duration. If all
     * checks pass, it applies the delay to the flight's departure time.
     *
     * @param flightId the ID of the flight to delay (format: 3 uppercase
     * letters followed by 3 digits, e.g., ABC123)
     * @param delayHours the number of hours to delay the flight (must be
     * numeric and non-negative)
     * @param delayMinutes the number of minutes to delay the flight (must be
     * numeric and non-negative)
     * @return a {@link Response} object with:
     * <ul>
     * <li>Status {@code BAD_REQUEST} if inputs are invalid (format, missing
     * data, or delay equals 00:00)</li>
     * <li>Status {@code NOT_FOUND} if the flight is not found</li>
     * <li>Status {@code OK} if the flight was successfully delayed (returns the
     * updated flight)</li>
     * <li>Status {@code INTERNAL_SERVER_ERROR} if an unexpected error occurs
     * during delay</li>
     * </ul>
     */
    public Response delayFlight(String flightId, String delayHours, String delayMinutes) {

        // Validar ID de vuelo
        if (flightId == null || !flightId.matches("^[A-Z]{3}\\d{3}$")) {
            return new Response("Flight ID must follow the format XXXYYY (3 uppercase letters and 3 digits)", Status.BAD_REQUEST);
        }

        Flight flight = managerFlight.getById(flightId);
        if (flight == null) {
            return new Response("Flight not found", Status.NOT_FOUND);
        }

        int hours, minutes;
        try {
            hours = Integer.parseInt(delayHours);
            minutes = Integer.parseInt(delayMinutes);

            if (hours < 0 || minutes < 0 || (hours == 0 && minutes == 0)) {
                return new Response("Delay time must be greater than 00:00", Status.BAD_REQUEST);
            }

        } catch (NumberFormatException e) {
            return new Response("Delay time must be numeric", Status.BAD_REQUEST);
        }

        try {
            flight.delay(hours, minutes);
            // aqui se tiene que usar el observer
            if (observer != null) {
                observer.update(managerFlight.getAll());
            }
            return new Response("Flight delayed successfully", Status.OK, flight);
        } catch (Exception e) {
            return new Response("Error delaying flight", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllFlights() {
        List<Flight> originals = managerFlight.getAll();

        if (originals == null || originals.isEmpty()) {
            return new Response("No Flight available", Status.OK, Collections.emptyList());
        }

        try {
            List<Flight> copies = originals.stream()
                    .filter(Objects::nonNull)
                    .map(Flight::clone)
                    .collect(Collectors.toList());
            return new Response("Flight retrieved successfully", Status.OK, copies);
        } catch (Exception e) {
            return new Response("Error cloning locations", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
