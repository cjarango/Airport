package core.model.observables.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Plane;
import java.util.ArrayList;
import java.util.List;
import core.model.observables.interfaces.Observable;


public class ObservablePlane implements Observable<List<Plane>> {

    private final List<Observer<List<Plane>>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<List<Plane>> o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<List<Plane>> o) {
        observers.remove(o);
    }

    @Override
    // Notifica a todos los observers con la lista actualizada de aviones
    public void notifyObservers(List<Plane> planes) {
        for (Observer<List<Plane>> o : observers) {
            o.update(planes);
        }
    }
}
