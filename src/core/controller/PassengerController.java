package core.controller;

import core.controller.utils.Response;
import core.controller.utils.Status;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PassengerController {

    public static Response createPassenger(String id, String firstname,
            String lastname, String birthDate, String countryPhoneCode,
            String phone, String country) { // Se creara un método para añadir los vuelos
        try {
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
            LocalDate birth;
            try {
                birth = LocalDate.parse(birthDate); // Formato esperado: yyyy-MM-dd
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

            // TODO: Implementar almacenamiento real
            // Passenger passenger = new Passenger(idLong, firstname, lastname, birth, code, phoneNum, country);
            // Storage.getInstance().addPassenger(passenger);
            return new Response("Passenger creation logic not implemented yet", Status.NOT_IMPLEMENTED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

}
