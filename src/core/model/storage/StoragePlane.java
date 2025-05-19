package core.model.storage;

import core.model.Plane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoragePlane implements StorageInterface<Plane, String> {

    // Instancia Singleton
    private static StoragePlane instance;

    private ArrayList<Plane> planes;

    private StoragePlane() {
        this.planes = new ArrayList<>();
    }

    public static StoragePlane getInstance() {
        if (instance == null) {
            instance = new StoragePlane();
        }
        return instance;
    }

    public List<Plane> getPlanes() {
        return new ArrayList<>(this.planes);
    }

    /**
     * Agrega un avión a la lista si no existe otro avión con el mismo ID. Luego
     * ordena la lista por ID para mantener el orden.
     *
     * @param plane El avión a agregar.
     * @return {@code true} si el avión fue agregado exitosamente; {@code false}
     * si ya existe un avión con el mismo ID.
     */
    @Override
    public boolean add(Plane plane) {
        for (Plane p : this.planes) {
            if (p.getId() == null ? plane.getId() == null : p.getId().equals(plane.getId())) {
                return false;
            }
        }
        this.planes.add(plane);
        this.planes.sort(Comparator.comparing(Plane::getId)); // Ordenar la lista después de agregar
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
