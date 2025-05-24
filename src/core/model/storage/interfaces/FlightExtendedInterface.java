
package core.model.storage.interfaces;

import core.model.entity.Passenger;
import java.util.List;


public interface FlightExtendedInterface <T, ID> extends StorageInterface<T, ID>{
    boolean addPassenger(T item, Passenger passenger);
    List<Passenger> getAllPassenger(T item);
}
