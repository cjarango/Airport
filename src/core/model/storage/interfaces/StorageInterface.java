package core.model.storage.interfaces;

import java.util.List;

public interface StorageInterface<T, ID> {
    boolean add(T item);
    T getById(ID id);
    List<T> getAll();
}