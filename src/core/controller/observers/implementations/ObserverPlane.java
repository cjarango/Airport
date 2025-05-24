package core.controller.observers.implementations;

import core.controller.observers.interfaces.Observer;
import core.model.entity.Plane;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ObserverPlane implements Observer<List<Plane>> {

    private static ObserverPlane instance;
    private final JTable table;

    private ObserverPlane(JTable table) {
        this.table = table;
    }
    
    public static ObserverPlane getInstance(JTable table) {
        if (instance == null) {
            instance = new ObserverPlane(table);
        }
        return instance;
    }

    @Override
    public void update(List<Plane> planes) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpiar tabla

        for (Plane plane : planes) {
            model.addRow(new Object[]{
                plane.getId(),
                plane.getBrand(),
                plane.getModel(),
                plane.getMaxCapacity(),
                plane.getAirline(),
                plane.getNumFlights()
            });
        }
    }
}
