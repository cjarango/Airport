package core.model.manager.implementations;

import core.model.entity.Passenger;
import core.model.manager.interfaces.ManagerInterface;
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
 * </p>
 */
public class ManagerPassenger implements ManagerInterface<Passenger, Long> {

    private static ManagerPassenger instance;
     private final UpdatableStorageInterface<Passenger, Long> storage;

    private ManagerPassenger(UpdatableStorageInterface<Passenger, Long> storage) {
        this.storage = storage;
    }

    /**
     * Devuelve la instancia única de ManagerPassenger. Si no existe, la crea
     * con el Storage proporcionado.
     *
     * @param storage Implementación concreta de almacenamiento.
     * @return Instancia única de {@code ManagerPassenger}.
     */
    public static synchronized ManagerPassenger getInstance(UpdatableStorageInterface<Passenger, Long> storage) {
        if (instance == null) {
            instance = new ManagerPassenger(storage);
        }
        return instance;
    }
    
     public static ManagerPassenger getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    /**
     * Agrega un nuevo pasajero al sistema si no existe otro con el mismo ID.
     *
     * @param passenger El pasajero a agregar.
     * @return {@code true} si se agregó correctamente, {@code false} si ya
     * existía una con el mismo ID.
     */
    @Override
    public boolean add(Passenger passenger) {
        return storage.add(passenger);
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
     * Actualiza los datos de un pasajero existente.
     *
     * @param passenger El pasajero con la información actualizada.
     * @return {@code true} si el pasajero fue actualizado correctamente,
     * {@code false} si no existe.
     */
    public boolean update(Passenger passenger) {
        return storage.update(passenger);
    }

}
