package main;
import core.view.AirportFrame;


public class Main {
    public static void main(String[] args) {
        try {
            UIConfigurator.configure();

            AppInitializer app = new AppInitializer();

            java.awt.EventQueue.invokeLater(() -> {
                new AirportFrame(
                        app.getPlaneController(),
                        app.getLocationController(),
                        app.getPassengerController(),
                        app.getFlightController()
                ).setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
