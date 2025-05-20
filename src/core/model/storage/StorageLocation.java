package core.model.storage;

import core.model.Location;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StorageLocation implements StorageInterface<Location, String> {

    // Instancia Singleton
    private static StorageLocation instance;

    private ArrayList<Location> locations;

    private StorageLocation() {
        this.locations = new ArrayList<>();
    }

    public static StorageLocation getInstance() {
        if (instance == null) {
            instance = new StorageLocation();
        }
        return instance;
    }

    public List<Location> getLocations() {
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
