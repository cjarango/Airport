package core.model.storage;

public interface StorageInterface<T, ID> {
    boolean add(T item);
    T getById(ID id);
}
