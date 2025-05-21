
package core.model.storage.interfaces;


public interface UpdatableStorageInterface<T, ID> extends StorageInterface<T, ID> {
    boolean update(T item);
}
