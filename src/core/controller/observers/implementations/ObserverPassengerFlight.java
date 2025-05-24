
package core.controller.observers.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Flight;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class ObserverPassengerFlight implements Observer<List<Flight>> {

    private final JTable table;
    private static ObserverPassengerFlight instance;

    private ObserverPassengerFlight(JTable table) {
        this.table = table;
    }
    
    public static ObserverPassengerFlight getInstance(JTable table) {
        if (instance == null) {
            instance = new ObserverPassengerFlight(table);
        }
        return instance;
    }


    @Override
    public void update(List<Flight> flights) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de actualizar

        for (Flight flight : flights) {
            model.addRow(new Object[]{
                flight.getId(),
                flight.getDepartureDate(),
                flight.calculateArrivalDate()
            });
        }
    }
}