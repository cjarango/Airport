package core.model.manager.implementations;

import core.model.entity.Plane;
import core.model.manager.interfaces.ManagerInterface;
import core.model.storage.interfaces.StorageInterface;
import java.util.List;

/**
 * ManagerPlane se encarga de gestionar las operaciones relacionadas con las
 * entidades {@link Plane}. Implementa el patrón Singleton para garantizar una
 * única instancia durante la ejecución del programa.
 *
 * <p>
 * Este manager actúa como intermediario entre los controladores y la capa de
 * almacenamiento, ocultando los detalles de implementación del
 * almacenamiento.</p>
 */
public class ManagerPlane implements ManagerInterface<Plane, String> {

    private static ManagerPlane instance;
    private final StorageInterface<Plane, String> storage;

    private ManagerPlane(StorageInterface<Plane, String> storage) {
        this.storage = storage;
    }

    public static synchronized ManagerPlane getInstance(StorageInterface<Plane, String> storage) {
        if (instance == null) {
            instance = new ManagerPlane(storage);
        }
        return instance;
    }

    /**
     * Agrega un nuevo avión al sistema si no existe otro con el mismo ID.
     *
     * @param plane El avión a agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existía una con el mismo ID.
     */
    @Override
    public boolean add(Plane plane) {
        return storage.add(plane);
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
