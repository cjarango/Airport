package core.model.manager.implementations;

import core.model.entity.Flight;
import core.model.entity.Passenger;
import core.model.manager.interfaces.ManagerInterface;
import core.model.observables.implementations.ObservablePassenger;
import core.model.observables.implementations.ObservablePassengerFlight;
import core.model.storage.interfaces.UpdatableStorageInterface;
import java.util.List;

/**
 * ManagerPassenger se encarga de gestionar las operaciones relacionadas con las
 * entidades {@link Passenger}. Implementa el patrón Singleton para garantizar
 * una única instancia durante la ejecución del programa.
 *
 * <p>
 * Este manager actúa como intermediario entre los controladores y la capa de
 * almacenamiento, ocultando los detalles de implementación del almacenamiento.
 * Además, notifica a los observadores ante cambios relevantes en la colección
 * de pasajeros.
 * </p>
 */
public class ManagerPassenger implements ManagerInterface<Passenger, Long> {

    private static ManagerPassenger instance;
    private final UpdatableStorageInterface<Passenger, Long> storage;
    private final ObservablePassenger observable;
    private final ObservablePassengerFlight observableFlights;

    private ManagerPassenger(UpdatableStorageInterface<Passenger, Long> storage, ObservablePassenger observable,
            ObservablePassengerFlight observableFligths) {
        this.storage = storage;
        this.observable = observable;
        this.observableFlights = observableFligths;
    }

    /**
     * Devuelve la instancia única de ManagerPassenger.Si no existe, la crea
 con el Storage y Observable proporcionados.
     *
     * @param storage Implementación concreta de almacenamiento.
     * @param observable Instancia para notificar cambios a observadores.
     * @param observableFligths
     * @return Instancia única de {@code ManagerPassenger}.
     */
    public static synchronized ManagerPassenger getInstance(UpdatableStorageInterface<Passenger, Long> storage, ObservablePassenger observable,
            ObservablePassengerFlight observableFligths) {
        if (instance == null) {
            instance = new ManagerPassenger(storage, observable, observableFligths);
        }
        return instance;
    }

    /**
     * Devuelve la instancia única de ManagerPassenger si ya fue creada, o null.
     *
     * @return Instancia única de {@code ManagerPassenger} o {@code null} si no
     * existe.
     */
    public static ManagerPassenger getInstance() {
        return instance;
    }

    /**
     * Agrega un nuevo pasajero al sistema si no existe otro con el mismo ID.
     * Notifica a los observadores si se agregó correctamente.
     *
     * @param passenger El pasajero a agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existía uno con el mismo ID.
     */
    @Override
    public boolean add(Passenger passenger) {
        boolean added = storage.add(passenger);
        if (added) {
            List<Passenger> passengers = storage.getAll(); // notifica cuando se agrega un pasejero
            observable.notifyObservers(passengers);
        }
        return added;
    }

    /**
     * Recupera un pasajero mediante su ID.
     *
     * @param id El identificador único del pasajero.
     * @return El {@link Passenger} correspondiente, o {@code null} si no se
     * encuentra.
     */
    @Override
    public Passenger getById(Long id) {
        return storage.getById(id);
    }

    /**
     * Obtiene todos los pasajeros almacenados actualmente.
     *
     * @return Lista de todos los pasajeros registrados. Nunca es {@code null},
     * pero puede estar vacía.
     */
    @Override
    public List<Passenger> getAll() {
        return storage.getAll();
    }

    /**
     * Actualiza los datos de un pasajero existente. Notifica a los observadores
     * si la actualización fue exitosa.
     *
     * @param passenger El pasajero con la información actualizada.
     * @return {@code true} si el pasajero fue actualizado correctamente,
     * {@code false} si no existe.
     */
    public boolean update(Passenger passenger) {
        boolean updated = storage.update(passenger);
        if (updated) {
            List<Passenger> passengers = storage.getAll();  // notifica cuando se actualiza el pasejero
            observable.notifyObservers(passengers);
        }
        return updated;
    }

    /**
     * Agrega un vuelo a un pasajero identificado por su ID.Notifica a los
     * observadores si el vuelo fue agregado correctamente.
     *
     * @param passenger
     * @param flight Vuelo a agregar.
     * @return {@code true} si el vuelo fue agregado correctamente,
     * {@code false} si no.
     */
    public boolean addFlightToPassenger(Passenger passenger, Flight flight) {
        boolean added = storage.addFlight(passenger, flight);
        if (added) {
            List<Passenger> passengers = storage.getAll(); 
            observable.notifyObservers(passengers);
            
            List<Flight> flights = storage.getAllFligts(passenger); // notifica cuando se actualiza los vuelos del pasajero
            observableFlights.notifyObservers(flights);
        }
        return added;
    }
}
