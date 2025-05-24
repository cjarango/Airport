package core.controller.observers.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Flight;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ObserverFlight implements Observer<List<Flight>> {

    private static ObserverFlight instance;
    private final JTable table;

    // Constructor privado para evitar instanciación externa
    private ObserverFlight(JTable table) {
        this.table = table;
    }

    // Método para obtener la instancia, solo se crea la primera vez
    public static ObserverFlight getInstance(JTable table) {
        if (instance == null) {
            instance = new ObserverFlight(table);
        }
        return instance;
    }

    @Override
    public void update(List<Flight> flights) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpiar tabla

        for (Flight flight : flights) {
            model.addRow(new Object[]{
                flight.getId(),
                flight.getDepartureLocation().getAirportId(),
                flight.getArrivalLocation().getAirportId(),
                (flight.getScaleLocation() == null ? "-" : flight.getScaleLocation().getAirportId()),
                flight.getDepartureDate(),
                flight.calculateArrivalDate(),
                flight.getPlane().getId(),
                flight.getNumPassengers()
            });
        }
    }
}
