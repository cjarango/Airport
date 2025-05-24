package core.model.storage.implementations;

import core.model.entity.Flight;
import core.model.entity.Passenger;
import core.model.storage.interfaces.FlightExtendedInterface;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StorageFlight implements FlightExtendedInterface<Flight, String> {

    private final List<Flight> flights;

    public StorageFlight() {
        this.flights = new ArrayList<>();
    }

    /**
     * Agrega un vuelo si no existe otro con el mismo ID.
     *
     * @param flight El vuelo a agregar
     * @return true si se agregó exitosamente, false si ya existe un vuelo con
     * ese ID
     */
    @Override
    public boolean add(Flight flight) {
        // Verificar si ya existe usando búsqueda binaria (más eficiente)
        if (getById(flight.getId()) != null) {
            return false;
        }

        this.flights.add(flight);
        // Ordenar por fecha/hora de salida (más antiguo a más nuevo)
        this.flights.sort(Comparator.comparing(Flight::getDepartureDate));
        return true;
    }

    @Override
    public List<Passenger> getAllPassenger(Flight flight) {
        return new ArrayList<>(flight.getPassengers());
    }

    /**
     * Busca un vuelo en la lista por su ID usando búsqueda secuencial.
     *
     * @param id El ID del vuelo a buscar. Se asume que no es null ni inválido.
     * @return El objeto Fligth con el ID especificado, o null si no se
     * encuentra.
     */
    @Override
    public Flight getById(String id) {
        for (Flight flight : this.flights) {
            if (flight.getId().equals(id)) {
                return flight;
            }
        }
        return null;
    }

    /**
     * Returns a defensive copy of all flights currently stored.
     *
     * <p>
     * <b>Important:</b> This method returns a new {@link ArrayList} containing
     * all flights to prevent external modifications to the internal storage.
     * Changes to the returned list will not affect the repository's data.</p>
     *
     * @return A new {@link List} containing all stored flights. The list is
     * empty if no flights are present, never {@code null}.
     */
    @Override
    public List<Flight> getAll() {
        return new ArrayList<>(this.flights);
    }

    @Override
    public boolean addPassenger(Flight flight, Passenger passenger) {
        if (passenger == null || flight == null) {
            return false;
        }
        Flight inList = getById(flight.getId());
        if (inList == null) {
            return false;
        }
        // Verificar si el pasajero ya está en la lista de pasajeros del vuelo
        for (Passenger p : inList.getPassengers()) {
            if (p.getId() == passenger.getId()) {
                return false;
            }
        }

        for (Flight f : passenger.getFlights()) {
            if (f.getId().equals(flight.getId())) {
                return false;
            }
        }

        passenger.addFlight(flight);   // Ya no se recursiona
        flight.addPassenger(passenger); // Ya no se recursiona
        return true;
    }

}
