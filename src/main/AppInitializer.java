package main;

import core.controller.controllers.FlightController;
import core.controller.controllers.LocationController;
import core.controller.controllers.PassengerController;
import core.controller.controllers.PlaneController;
import core.model.entity.Flight;
import core.model.entity.Location;
import core.model.entity.Passenger;
import core.model.entity.Plane;
import core.model.manager.implementations.ManagerFlight;
import core.model.manager.implementations.ManagerLocation;
import core.model.manager.implementations.ManagerPassenger;
import core.model.manager.implementations.ManagerPlane;
import core.model.observables.implementations.ObservableFlight;
import core.model.observables.implementations.ObservableLocation;
import core.model.observables.implementations.ObservablePassenger;
import core.model.observables.implementations.ObservablePassengerFlight;
import core.model.observables.implementations.ObservablePlane;
import core.model.storage.implementations.StorageFlight;
import core.model.storage.implementations.StorageLocation;
import core.model.storage.implementations.StoragePassenger;
import core.model.storage.implementations.StoragePlane;
import core.utils.loader.implimentations.FlightLoader;
import core.utils.loader.implimentations.LocationLoader;
import core.utils.loader.implimentations.PassengerLoader;
import core.utils.loader.implimentations.PlaneLoader;
import core.utils.loader.interfaces.DataLoader;
import core.utils.parser.implimentations.FlightParser;
import core.utils.parser.implimentations.LocationParser;
import core.utils.parser.implimentations.PassengerParser;
import core.utils.parser.implimentations.PlaneParser;
import core.utils.parser.interfaces.JsonParser;

public class AppInitializer {

    private final ManagerPlane managerPlane;
    private final ManagerLocation managerLocation;
    private final ManagerFlight managerFlight;
    private final ManagerPassenger managerPassenger;

    private final PlaneController planeController;
    private final LocationController locationController;
    private final PassengerController passengerController;
    private final FlightController flightController;

    public AppInitializer() throws Exception {
        // 1. Crear storages
        StoragePlane storagePlane = new StoragePlane();
        StorageLocation storageLocation = new StorageLocation();
        StorageFlight storageFlight = new StorageFlight();
        StoragePassenger storagePassenger = new StoragePassenger();

        // 2. Crear observables
        ObservablePlane observablePlane = new ObservablePlane();
        ObservableLocation observableLocation = new ObservableLocation();
        ObservableFlight observableFlight = new ObservableFlight();
        ObservablePassenger observablePassenger = new ObservablePassenger();
        ObservablePassengerFlight observablePassengerFlight = new ObservablePassengerFlight();

        // 3. Crear managers (inyectar storages y observables)
        this.managerPlane = ManagerPlane.getInstance(storagePlane, observablePlane);
        this.managerLocation = ManagerLocation.getInstance(storageLocation, observableLocation);
        this.managerFlight = ManagerFlight.getInstance(storageFlight, observableFlight, observablePassenger);
        this.managerPassenger = ManagerPassenger.getInstance(storagePassenger, observablePassenger, observablePassengerFlight);

        // 4. Crear parsers (inyectar managers si es necesario)
        JsonParser<Plane> planeParser = new PlaneParser();
        JsonParser<Location> locationParser = new LocationParser();
        JsonParser<Passenger> passengerParser = new PassengerParser();
        JsonParser<Flight> flightParser = new FlightParser(managerPlane, managerLocation);

        // 5. Crear loaders (inyección de parsers y managers)
        DataLoader<Plane> planeLoader = new PlaneLoader(planeParser, managerPlane);
        DataLoader<Location> locationLoader = new LocationLoader(locationParser, managerLocation);
        DataLoader<Passenger> passengerLoader = new PassengerLoader(passengerParser, managerPassenger);
        DataLoader<Flight> flightLoader = new FlightLoader(flightParser, managerFlight);

        // 6. Cargar datos desde archivos JSON
        planeLoader.loadFromFile("json/planes.json");
        locationLoader.loadFromFile("json/locations.json");
        passengerLoader.loadFromFile("json/passengers.json");
        flightLoader.loadFromFile("json/flights.json");

        // 7. Crear controladores (inyección de managers)
        this.planeController = new PlaneController(managerPlane);
        this.locationController = new LocationController(managerLocation);
        this.passengerController = new PassengerController(managerPassenger);
        this.flightController = new FlightController(managerFlight, managerPlane, managerLocation, managerPassenger);
    }

    public PlaneController getPlaneController() {
        return planeController;
    }

    public LocationController getLocationController() {
        return locationController;
    }

    public PassengerController getPassengerController() {
        return passengerController;
    }

    public FlightController getFlightController() {
        return flightController;
    }

}
