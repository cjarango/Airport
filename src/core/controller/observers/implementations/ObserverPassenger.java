
package core.controller.observers.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Passenger;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ObserverPassenger implements Observer<List<Passenger>> {

    private final JTable table;
    private static ObserverPassenger instance;

    private ObserverPassenger(JTable table) {
        this.table = table;
    }
    
    public static ObserverPassenger getInstance(JTable table) {
        if (instance == null) {
            instance = new ObserverPassenger(table);
        }
        return instance;
    }

    @Override
    public void update(List<Passenger> passengers) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpiar tabla

        for (Passenger passenger : passengers) {
            model.addRow(new Object[]{
                passenger.getId(),
                passenger.getFullname(),
                passenger.getBirthDate(),
                passenger.calculateAge(),
                passenger.generateFullPhone(),
                passenger.getCountry(),
                passenger.getNumFlights()
            });
        }
    }
}
