package core.model.manager.implementations;

import core.model.entity.Flight;
import core.model.manager.interfaces.ManagerInterface;
import core.model.storage.interfaces.StorageInterface;
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
    private final StorageInterface<Flight, String> storage;

    private ManagerFlight(StorageInterface<Flight, String> storage) {
        this.storage = storage;
    }

    /**
     * Devuelve la instancia única de ManagerFlight.Si no existe, la crea.
     *
     * @param storage
     * @return Instancia única de {@code ManagerFlight}.
     */
    public static ManagerFlight getInstance(StorageInterface<Flight, String> storage) {
        if (instance == null) {
            instance = new ManagerFlight(storage);
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
        return storage.add(flight);
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
     * @return Lista de todos los vuelos registrados. Lista con todos los vuelos
     * almacenados. Nunca es {@code null}, pero puede estar vacía.
     */
    @Override
    public List<Flight> getAll() {
        return storage.getAll();
    }
}
