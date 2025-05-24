package core.controller.controllers;

import core.controller.observers.implementations.ObserverPassenger;
import core.controller.utils.Response;
import core.controller.utils.Status;
import core.model.entity.Flight;
import core.model.entity.Passenger;
import core.model.manager.implementations.ManagerPassenger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JTable;

public class PassengerController {

    private final ManagerPassenger managerPassenger;
    private ObserverPassenger observer;

    public PassengerController(ManagerPassenger managerPassenger) {
        this.managerPassenger = managerPassenger;
        this.observer = null;
    }

    public void setObserver(JTable table) {
        if (this.observer == null) {
            this.observer = ObserverPassenger.getInstance(table);
        }
    }

    private Response validatePassengerData(String id, String firstname,
            String lastname, String birthDate, String countryPhoneCode,
            String phone, String country) {

        // Validar ID
        long idLong;
        try {
            idLong = Long.parseLong(id);
            if (idLong < 0 || id.length() > 15) {
                return new Response("Id must be >= 0 and at most 15 digits", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            return new Response("Id must be numeric", Status.BAD_REQUEST);
        }

        // Validar nombre y apellido
        if (firstname == null || firstname.trim().isEmpty()) {
            return new Response("Firstname must not be empty", Status.BAD_REQUEST);
        }
        if (lastname == null || lastname.trim().isEmpty()) {
            return new Response("Lastname must not be empty", Status.BAD_REQUEST);
        }

        // Validar fecha de nacimiento
        try {
            LocalDate.parse(birthDate); // Formato esperado: yyyy-MM-dd
        } catch (DateTimeParseException ex) {
            return new Response("Invalid birth date format (expected yyyy-MM-dd)", Status.BAD_REQUEST);
        }

        // Validar código del país
        int code;
        try {
            code = Integer.parseInt(countryPhoneCode);
            if (code < 0 || countryPhoneCode.length() > 3) {
                return new Response("Country phone code must be >= 0 and at most 3 digits", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            return new Response("Country phone code must be numeric", Status.BAD_REQUEST);
        }

        // Validar teléfono
        long phoneNum;
        try {
            phoneNum = Long.parseLong(phone);
            if (phoneNum < 0 || phone.length() > 11) {
                return new Response("Phone number must be >= 0 and at most 11 digits", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException ex) {
            return new Response("Phone number must be numeric", Status.BAD_REQUEST);
        }

        // Validar país
        if (country == null || country.trim().isEmpty()) {
            return new Response("Country must not be empty", Status.BAD_REQUEST);
        }

        return null;
    }

    private Passenger parsePassenger(String id, String firstname,
            String lastname, String birthDate, String countryPhoneCode,
            String phone, String country) {

        return new Passenger(
                Long.parseLong(id),
                firstname,
                lastname,
                LocalDate.parse(birthDate),
                Integer.parseInt(countryPhoneCode),
                Long.parseLong(phone),
                country
        );
    }

    public Response createPassenger(String id, String firstname,
            String lastname, String birthDate, String countryPhoneCode,
            String phone, String country) {

        try {
            Response validationResponse = validatePassengerData(id, firstname, lastname,
                    birthDate, countryPhoneCode, phone, country);
            if (validationResponse != null) {
                return validationResponse;
            }

            Passenger newPassenger = parsePassenger(id, firstname, lastname,
                    birthDate, countryPhoneCode, phone, country);

            if (!managerPassenger.add(newPassenger)) {
                return new Response("A passenger with that id already exists", Status.BAD_REQUEST);
            }

            if (observer != null) {
                observer.update(managerPassenger.getAll());
            }

            return new Response("Passenger created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response updatePassenger(String id, String firstname,
            String lastname, String birthDate, String countryPhoneCode,
            String phone, String country) {

        try {
            Response validationResponse = validatePassengerData(id, firstname, lastname,
                    birthDate, countryPhoneCode, phone, country);
            if (validationResponse != null) {
                return validationResponse;
            }

            Passenger updatedPassenger = parsePassenger(id, firstname, lastname,
                    birthDate, countryPhoneCode, phone, country);

            if (!managerPassenger.update(updatedPassenger)) {
                return new Response("Passenger not found", Status.NOT_FOUND);
            }

            if (observer != null) {
                observer.update(managerPassenger.getAll());
            }

            return new Response("Passenger updated successfully", Status.OK);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getAllPassengers() {
        try {
            List<Passenger> passengers = managerPassenger.getAll();

            if (passengers == null || passengers.isEmpty()) {
                return new Response("No passengers available", Status.OK, List.of());
            }

            List<Passenger> copies = passengers.stream()
                    .filter(Objects::nonNull)
                    .map(Passenger::clone)
                    .collect(Collectors.toList());

            return new Response("Passengers retrieved successfully", Status.OK, copies);
        } catch (Exception e) {
            return new Response("Error retrieving passengers", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getFlightsByPassengerId(String id) {
        try {
            long idLong;
            try {
                idLong = Long.parseLong(id);
                if (idLong < 0 || id.length() > 15) {
                    return new Response("Id must be >= 0 and at most 15 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            Passenger passenger = managerPassenger.getById(idLong);
            if (passenger == null) {
                return new Response("Passenger not found", Status.NOT_FOUND);
            }

            List<Flight> flights = passenger.getFlights();
            List<Flight> copies = flights.stream()
                    .filter(Objects::nonNull)
                    .map(Flight::clone)
                    .collect(Collectors.toList());

            return new Response("Flights retrieved successfully", Status.OK, copies);

        } catch (Exception e) {
            return new Response("Error retrieving flights", Status.INTERNAL_SERVER_ERROR);
        }
    }

}
