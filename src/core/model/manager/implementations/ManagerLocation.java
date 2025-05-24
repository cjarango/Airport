package core.model.manager.implementations;

import core.model.entity.Location;
import core.model.manager.interfaces.ManagerInterface;
import core.model.observables.implementations.ObservableLocation;
import core.model.storage.interfaces.StorageInterface;
import java.util.List;

/**
 * ManagerLocation se encarga de gestionar las operaciones relacionadas con las
 * entidades {@link Location}. Implementa el patrón Singleton para garantizar
 * una única instancia durante la ejecución del programa.
 *
 * <p>
 * Este manager actúa como intermediario entre los controladores y la capa de
 * almacenamiento, ocultando los detalles de implementación del
 * almacenamiento.</p>
 */
public class ManagerLocation implements ManagerInterface<Location, String> {

    private static ManagerLocation instance;
    private final StorageInterface<Location, String> storage;
    private final ObservableLocation observable;

    private ManagerLocation(StorageInterface<Location, String> storage, ObservableLocation observable) {
        this.storage = storage;
        this.observable = observable;
    }

    /**
     * Devuelve la instancia única de ManagerLocation. Si no existe, la crea.
     *
     * @param storage la interfaz de almacenamiento usada para persistencia.
     * @param observable la instancia de ObservableLocation para notificaciones.
     * @return Instancia única de {@code ManagerLocation}.
     */
    public static ManagerLocation getInstance(StorageInterface<Location, String> storage, ObservableLocation observable) {
        if (instance == null) {
            instance = new ManagerLocation(storage, observable);
        }
        return instance;
    }

    /**
     * Devuelve la instancia única de ManagerLocation si ya fue creada, o null.
     *
     * @return Instancia única de {@code ManagerLocation} o {@code null} si no
     * existe.
     */
    public static ManagerLocation getInstance() {
        return instance;
    }

    /**
     * Agrega una nueva ubicación al sistema si no existe otra con el mismo ID.
     * Notifica a los observadores si se agregó correctamente.
     *
     * @param location La ubicación a agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existía una con el mismo ID.
     */
    @Override
    public boolean add(Location location) {
        boolean added = storage.add(location);
        if (added) {
            List<Location> locations = storage.getAll(); // obtener lista actualizada
            observable.notifyObservers(locations);       // pasar la lista actualizada
        }
        return added;
    }

    /**
     * Recupera una ubicación mediante su ID.
     *
     * @param id El identificador único de la ubicación.
     * @return La {@link Location} correspondiente, o {@code null} si no se
     * encuentra.
     */
    @Override
    public Location getById(String id) {
        return storage.getById(id);
    }

    /**
     * Obtiene todas las ubicaciones almacenadas actualmente.
     *
     * @return Lista de todas las ubicaciones registradas. Nunca es
     * {@code null}.
     */
    @Override
    public List<Location> getAll() {
        return storage.getAll();
    }
}
