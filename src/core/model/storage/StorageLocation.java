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
     * Agrega un avión a la lista si no existe otro avión con el mismo ID. Luego
     * ordena la lista por ID para mantener el orden.
     *
     * @param location El aeropuerto a agregar.
     * @return {@code true} si el aeropuerto fue agregado exitosamente; {@code false}
     * si ya existe un avión con el mismo ID.
     */
    @Override
    public boolean add(Location location) {
        for (Location p : this.locations) {
            if (p.getAirportId() == null ? location.getAirportId() == null : p.getAirportId().equals(location.getAirportId())) {
                return false;
            }
        }
        this.locations.add(location);
        this.locations.sort(Comparator.comparing(Location::getAirportId)); // Ordenar la lista después de agregar
        return true;
    }
    
    /**
     * Busca un auropuerto en la lista ordenada por su ID usando búsqueda binaria.
     *
     * @param id El ID del aeropuerto a buscar. Se asume que no es null ni inválido.
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
