package core.model.storage.implementations;

import core.model.storage.interfaces.StorageInterface;
import core.model.entity.Plane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoragePlane implements StorageInterface<Plane, String> {

    private final List<Plane> planes;

    public StoragePlane() {
        this.planes = new ArrayList<>();
    }

    /**
     * Returns a defensive copy of all planes currently stored.
     *
     * <p>
     * <b>Important:</b> This method returns a new {@link ArrayList} containing
     * all planes to prevent external modifications to the internal storage.
     * Changes to the returned list will not affect the repository's data.</p>
     *
     * @return A new {@link List} containing all stored planes. The list is
     * empty if no planes are present, never {@code null}.
     */
    @Override
    public List<Plane> getAll() {
        return new ArrayList<>(this.planes);
    }

    /**
     * Agrega un avión si no existe otro con el mismo ID.
     *
     * @param plane El avión a agregar
     * @return true si se agregó exitosamente, false si ya existe un avión con
     * ese ID
     */
    @Override
    public boolean add(Plane plane) {
        // Validación de entrada y verificación de existencia
        if (getById(plane.getId()) != null) {
            return false;
        }
        this.planes.add(plane);
        this.planes.sort(Comparator.comparing(Plane::getId)); // Mantener orden
        return true;
    }

    /**
     * Busca un avión en la lista ordenada por su ID usando búsqueda binaria.
     *
     * @param id El ID del avión a buscar. Se asume que no es null ni inválido.
     * @return El objeto Plane con el ID especificado, o null si no se
     * encuentra.
     */
    @Override
    public Plane getById(String id) {
        int index = Collections.binarySearch(
                this.planes,
                new Plane(id, null, null, 0, null),
                Comparator.comparing(Plane::getId)
        );

        if (index >= 0) {
            return this.planes.get(index);
        }
        return null;
    }

}
