package core.model.storage.implementations;

import core.model.storage.interfaces.StorageInterface;
import core.model.entity.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StorageLocation implements StorageInterface<Location, String> {
    private final List<Location> locations;

    public StorageLocation() {
        this.locations = new ArrayList<>();
    }

    
    /**
     * Returns a defensive copy of all locations currently stored.
     *
     * <p>
     * <b>Important:</b> This method returns a new {@link ArrayList} containing
     * all locations to prevent external modifications to the internal storage.
     * Changes to the returned list will not affect the repository's data.</p>
     *
     * @return A new {@link List} containing all stored locations. The list is
     * empty if no locations are present, never {@code null}.
     */
    @Override
    public List<Location> getAll() {
        return new ArrayList<>(this.locations);
    }

    /**
     * Agrega una ubicación si no existe otra con el mismo ID de aeropuerto.
     *
     * @param location La ubicación a agregar
     * @return true si se agregó exitosamente, false si ya existe una ubicación
     * con ese ID
     */
    @Override
    public boolean add(Location location) {
        // Validación de entrada y verificación de existencia usando búsqueda binaria
        if (getById(location.getAirportId()) != null) {
            return false;
        }

        this.locations.add(location);
        this.locations.sort(Comparator.comparing(Location::getAirportId)); // Mantener orden
        return true;
    }

    /**
     * Busca un auropuerto en la lista ordenada por su ID usando búsqueda
     * binaria.
     *
     * @param id El ID del aeropuerto a buscar. Se asume que no es null ni
     * inválido.
     * @return El objeto Location con el ID especificado, o null si no se
     * encuentra.
     */
    @Override
    public Location getById(String id) {
        int index = Collections.binarySearch(
                this.locations,
                new Location(id, null, null, null, 0.00, 0.00),
                Comparator.comparing(Location::getAirportId)
        );

        if (index >= 0) {
            return this.locations.get(index);
        }
        return null;
    }

}
