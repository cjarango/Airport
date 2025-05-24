package core.model.manager.implementations;

import core.model.entity.Flight;
import core.model.entity.Passenger;
import core.model.manager.interfaces.ManagerInterface;
import core.model.observables.implementations.ObservableFlight;
import core.model.observables.implementations.ObservablePassenger;
import core.model.storage.interfaces.FlightExtendedInterface;
import java.util.List;

/**
 * ManagerFlight se encarga de gestionar las operaciones relacionadas con las
 * entidades {@link Flight}. Implementa el patrón Singleton para garantizar una
 * única instancia activa durante el ciclo de vida del programa.
 *
 * <p>
 * Este manager actúa como intermediario entre los controladores y la capa de
 * almacenamiento, ocultando los detalles de implementación del
 * almacenamiento.</p>
 */
public class ManagerFlight implements ManagerInterface<Flight, String> {

    private static ManagerFlight instance;
    private final FlightExtendedInterface<Flight, String> storage;
    private final ObservableFlight observable;
    private final ObservablePassenger observablePassenger;

    private ManagerFlight(FlightExtendedInterface<Flight, String> storage, ObservableFlight observable,
            ObservablePassenger observablePassenger) {
        this.storage = storage;
        this.observable = observable;
        this.observablePassenger = observablePassenger;
    }

    /**
     * Devuelve la instancia única de ManagerFlight.Si no existe, la crea.
     *
     * @param storage
     * @param observable
     * @param observablePassenger
     * @return Instancia única de {@code ManagerFlight}.
     */
    public static ManagerFlight getInstance(FlightExtendedInterface<Flight, String> storage, ObservableFlight observable,
            ObservablePassenger observablePassenger) {
        if (instance == null) {
            instance = new ManagerFlight(storage, observable, observablePassenger);
        }
        return instance;
    }

    public static ManagerFlight getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    /**
     * Agrega un nuevo vuelo al sistema si no existe otro con el mismo ID.
     *
     * @param flight El vuelo a agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existía uno con el mismo ID.
     */
    @Override
    public boolean add(Flight flight) {
        boolean added = storage.add(flight);
        if (added) {
            List<Flight> flights = storage.getAll();  // Obtener la lista actualizada
            observable.notifyObservers(flights);      // Notificar con la lista nueva
        }
        return added;
    }

    /**
     * Recupera un vuelo mediante su ID.
     *
     * @param id El identificador único del vuelo.
     * @return El {@link Flight} correspondiente, o {@code null} si no se
     * encuentra.
     */
    @Override
    public Flight getById(String id) {
        return storage.getById(id);
    }

    /**
     * Obtiene todos los vuelos almacenados actualmente.
     *
     * @return Lista de todos los vuelos registrados. Nunca es {@code null},
     * pero puede estar vacía.
     */
    @Override
    public List<Flight> getAll() {
        return storage.getAll();
    }
    
    public boolean addPassenger(Flight flight, Passenger passenger) {
        boolean added = storage.addPassenger(flight, passenger);
        if (added) {
            List<Passenger> passengers = storage.getAllPassenger(flight); 
            observablePassenger.notifyObservers(passengers);
            
            List<Flight> flights = storage.getAll();
            observable.notifyObservers(flights);
        }
        return added;
    }
}
