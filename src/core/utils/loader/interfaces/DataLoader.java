package core.utils.loader.interfaces;

public interface DataLoader<T> {
    void loadFromFile(String filepath) throws Exception;
}
