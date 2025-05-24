
package core.model.observables.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Location;
import java.util.ArrayList;
import java.util.List;
import core.model.observables.interfaces.Observable;


public class ObservableLocation implements Observable<List<Location>> {

    private final List<Observer<List<Location>>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<List<Location>> o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<List<Location>> o) {
        observers.remove(o);
    }

    @Override
    // Ahora este método recibe la lista actualizada para enviarla a los observers
    public void notifyObservers(List<Location> locations) {
        for (Observer<List<Location>> o : observers) {
            o.update(locations);
        }
    }
}
