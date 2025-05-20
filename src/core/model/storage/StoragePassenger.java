package core.model.storage;

import core.model.Passenger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoragePassenger implements StorageInterface<Passenger, Long> {

    // Instancia Singleton
    private static StoragePassenger instance;

    private ArrayList<Passenger> passengers;

    private StoragePassenger() {
        this.passengers = new ArrayList<>();
    }

    public static StoragePassenger getInstance() {
        if (instance == null) {
            instance = new StoragePassenger();
        }
        return instance;
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(this.passengers);
    }

    /**
     * Agrega un pasajero si no existe otro con el mismo ID.
     *
     * @param passenger El pasajero a agregar
     * @return true si se agregó exitosamente, false si ya existe un pasajero
     * con ese ID
     */
    @Override
    public boolean add(Passenger passenger) {
        // Validación de entrada y verificación de existencia
        if (getById(passenger.getId()) != null) {
            return false;
        }

        this.passengers.add(passenger);
        this.passengers.sort(Comparator.comparingLong(Passenger::getId)); // Mantener orden
        return true;
    }

    /**
     * Busca un pasajero en la lista ordenada por su ID usando búsqueda binaria.
     *
     * @param id El ID del pasajero a buscar. Se asume que no es null ni
     * inválido.
     * @return El objeto Passenger con el ID especificado, o null si no se
     * encuentra.
     */
    @Override
    public Passenger getById(Long id) {
        int index = Collections.binarySearch(
                this.passengers,
                new Passenger(id, null, null, null, 0, 0, null),
                Comparator.comparingLong(Passenger::getId)
        );

        if (index >= 0) {
            return this.passengers.get(index);
        }
        return null;
    }

    public boolean update(Passenger passenger) {
        Passenger inList = getById(passenger.getId());
        if (inList == null) {
            return false;
        } else {
            // Actualizamos los campos del pasajero existente
            inList.setFirstname(passenger.getFirstname());
            inList.setLastname(passenger.getLastname());
            inList.setBirthDate(passenger.getBirthDate());
            inList.setCountryPhoneCode(passenger.getCountryPhoneCode());
            inList.setPhone(passenger.getPhone());
            inList.setCountry(passenger.getCountry());
            return true;
        }
    }
}
