
package core.model.observables.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Passenger;
import java.util.ArrayList;
import java.util.List;
import core.model.observables.interfaces.Observable;


public class ObservablePassenger implements Observable<List<Passenger>> {

    private final List<Observer<List<Passenger>>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<List<Passenger>> o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<List<Passenger>> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(List<Passenger> passengers) {
        for (Observer<List<Passenger>> o : observers) {
            o.update(passengers);
        }
    }
}
