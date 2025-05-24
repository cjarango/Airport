package core.model.observables.interfaces;

import core.controller.observers.interfaces.Observer;

public interface Observable<T> {
    void addObserver(Observer<T> o);
    void removeObserver(Observer<T> o);
    void notifyObservers(T data);
}
