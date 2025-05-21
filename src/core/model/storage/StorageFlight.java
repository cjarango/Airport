package core.model.storage;

import core.model.Flight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StorageFlight implements StorageInterface<Flight, String> {

    private static StorageFlight instance;

    private ArrayList<Flight> flights;

    private StorageFlight() {
        this.flights = new ArrayList<>();
    }

    public static StorageFlight getInstance() {
        if (instance == null) {
            instance = new StorageFlight();
        }
        return instance;
    }

    public List<Flight> getLocations() {
        return new ArrayList<>(this.flights);
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
    
    public List<Flight> getFlights() {
        return new ArrayList<>(this.flights);
    }

}
