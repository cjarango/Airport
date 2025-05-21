package core.model.manager.interfaces;

import java.util.List;

public interface ManagerInterface<T, ID> {
    boolean add(T item);
    T getById(ID id);
    List<T> getAll();

}
