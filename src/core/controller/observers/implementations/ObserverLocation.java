package core.controller.observers.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Location;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ObserverLocation implements Observer<List<Location>> {

    private static ObserverLocation instance;
    private final JTable table;

    private ObserverLocation(JTable table) {
        this.table = table;
    }
    
    public static ObserverLocation getInstance(JTable table) {
        if (instance == null) {
            instance = new ObserverLocation(table);
        }
        return instance;
    }

    @Override
    public void update(List<Location> locations) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpiar la tabla

        for (Location location : locations) {
            model.addRow(new Object[]{
                location.getAirportId(),
                location.getAirportName(),
                location.getAirportCity(),
                location.getAirportCountry()
            });
        }
    }
}
