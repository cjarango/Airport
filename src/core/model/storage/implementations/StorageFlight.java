package core.model.storage.implementations;
import core.model.storage.interfaces.StorageInterface;
import core.model.entity.Flight;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StorageFlight implements StorageInterface<Flight, String> {

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

}
