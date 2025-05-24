
package core.model.storage.interfaces;

import core.model.entity.Flight;
import java.util.List;


public interface UpdatableStorageInterface<T, ID> extends StorageInterface<T, ID> {
    boolean update(T item);
    List<Flight> getAllFligts(T item);
    boolean addFlight(T item, Flight flight);
}
