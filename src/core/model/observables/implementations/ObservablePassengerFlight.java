
package core.model.observables.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Flight;
import core.model.observables.interfaces.Observable;
import java.util.ArrayList;
import java.util.List;


public class ObservablePassengerFlight implements Observable<List<Flight>> {

    private final List<Observer<List<Flight>>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<List<Flight>> o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<List<Flight>> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(List<Flight> flights) {
        for (Observer<List<Flight>> o : observers) {
            o.update(flights);
        }
    }
}
