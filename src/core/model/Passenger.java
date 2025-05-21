/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;

import core.model.Flight;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author edangulo
 */
public class Passenger {

    private final long id;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private int countryPhoneCode;
    private long phone;
    private String country;
    private ArrayList<Flight> flights;

    public Passenger(long id, String firstname, String lastname, LocalDate birthDate, int countryPhoneCode, long phone, String country) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.countryPhoneCode = countryPhoneCode;
        this.phone = phone;
        this.country = country;
        this.flights = new ArrayList<>();
    }

    /**
     * Verifica si el pasajero tiene asignado un vuelo específico
     *
     * @param flightID ID del vuelo (asumimos no null por validación previa)
     * @return true si el pasajero está en el vuelo
     */
    private boolean hasFlight(String flightID) {
        // Versión optimizada con early return
        for (Flight flight : this.flights) {
            if (flight.getId().equals(flightID)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Agrega un vuelo a la lista de vuelos del pasajero si aún no ha sido
     * agregado.
     * <p>
     * La lista de vuelos se mantiene ordenada por fecha de salida en orden
     * ascendente (de vuelos más antiguos a más recientes). Además, se actualiza
     * la relación bidireccional agregando este pasajero a la lista de pasajeros
     * del vuelo.
     *
     * @param flight el vuelo a agregar; no debe ser {@code null} y debe tener
     * un ID válido.
     * @return {@code true} si el vuelo fue agregado exitosamente; {@code false}
     * si el vuelo ya estaba asociado al pasajero.
     */
    public boolean addFlight(Flight flight) {
        if (!hasFlight(flight.getId())) {
            this.flights.add(flight);
            this.flights.sort(Comparator.comparing(Flight::getDepartureDate));
            flight.addPassenger(this);
            return true;
        } else {
            return false;
        }
    }

    public Passenger clone() {
        return new Passenger(
                this.id,
                this.firstname,
                this.lastname,
                this.birthDate,
                this.countryPhoneCode,
                this.phone,
                this.country
        );
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public long getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setCountryPhoneCode(int countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String generateFullPhone() {
        return "+" + countryPhoneCode + " " + phone;
    }

    public int calculateAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public int getNumFlights() {
        return flights.size();
    }

}
