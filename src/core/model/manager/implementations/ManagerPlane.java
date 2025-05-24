package core.model.manager.implementations;

import core.model.entity.Plane;
import core.model.manager.interfaces.ManagerInterface;
import core.model.observables.implementations.ObservablePlane;
import core.model.storage.interfaces.StorageInterface;
import java.util.List;

/**
 * ManagerPlane se encarga de gestionar las operaciones relacionadas con las
 * entidades {@link Plane}. Implementa el patrón Singleton para garantizar una
 * única instancia durante la ejecución del programa.
 *
 * <p>
 * Este manager actúa como intermediario entre los controladores y la capa de
 * almacenamiento, ocultando los detalles de implementación del almacenamiento.
 * Además, notifica a los observadores ante cambios relevantes en la colección
 * de aviones.
 * </p>
 */
public class ManagerPlane implements ManagerInterface<Plane, String> {

    private static ManagerPlane instance;
    private final StorageInterface<Plane, String> storage;
    private final ObservablePlane observable;

    private ManagerPlane(StorageInterface<Plane, String> storage, ObservablePlane observable) {
        this.storage = storage;
        this.observable = observable;
    }

    /**
     * Devuelve la instancia única de ManagerPlane. Si no existe, la crea con el
     * Storage y Observable proporcionados.
     *
     * @param storage Implementación concreta de almacenamiento.
     * @param observable Instancia para notificar cambios a observadores.
     * @return Instancia única de {@code ManagerPlane}.
     */
    public static synchronized ManagerPlane getInstance(StorageInterface<Plane, String> storage, ObservablePlane observable) {
        if (instance == null) {
            instance = new ManagerPlane(storage, observable);
        }
        return instance;
    }

    /**
     * Devuelve la instancia única de ManagerPlane si ya fue creada, o null.
     *
     * @return Instancia única de {@code ManagerPlane} o {@code null} si no
     * existe.
     */
    public static ManagerPlane getInstance() {
        return instance;
    }

    /**
     * Agrega un nuevo avión al sistema si no existe otro con el mismo ID.
     * Notifica a los observadores si se agregó correctamente.
     *
     * @param plane El avión a agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existía uno con el mismo ID.
     */
    @Override
    public boolean add(Plane plane) {
        boolean added = storage.add(plane);
        if (added) {
            List<Plane> planes = storage.getAll();  // obtener la lista actualizada
            observable.notifyObservers(planes);    // notificar pasando la lista
        }
        return added;
    }

    /**
     * Recupera un avión mediante su ID.
     *
     * @param id El identificador único del avión.
     * @return El {@link Plane} correspondiente, o {@code null} si no se
     * encuentra.
     */
    @Override
    public Plane getById(String id) {
        return storage.getById(id);
    }

    /**
     * Obtiene todos los aviones almacenados actualmente.
     *
     * @return Lista de todos los aviones registrados. Nunca es {@code null},
     * pero puede estar vacía.
     */
    @Override
    public List<Plane> getAll() {
        return storage.getAll();
    }
}
