package core.utils.parser.implimentations;

import core.model.entity.Flight;
import core.model.entity.Location;
import core.model.entity.Plane;
import core.model.manager.implementations.ManagerLocation;
import core.model.manager.implementations.ManagerPlane;
import core.utils.parser.interfaces.JsonParser;
import java.time.LocalDateTime;
import org.json.JSONObject;

public class FlightParser implements JsonParser<Flight> {

    private final ManagerPlane managerPlane;
    private final ManagerLocation managerLocation;

    public FlightParser(ManagerPlane managerPlane, ManagerLocation managerLocation) {
        this.managerPlane = managerPlane;
        this.managerLocation = managerLocation;
    }

    @Override
    public Flight parse(JSONObject obj) {

        String id = obj.getString("id");

        String planeId = obj.getString("plane");
        Plane plane = managerPlane.getById(planeId);

        String departureId = obj.getString("departureLocation");
        Location departureLocation = managerLocation.getById(departureId);

        String arrivalId = obj.getString("arrivalLocation");
        Location arrivalLocation = managerLocation.getById(arrivalId);

        // scaleLocation puede ser null
        Location scaleLocation = null;
        if (!obj.isNull("scaleLocation")) {
            String scaleId = obj.getString("scaleLocation");
            scaleLocation = managerLocation.getById(scaleId);
        }

        LocalDateTime departureDate = LocalDateTime.parse(obj.getString("departureDate"));
        int hoursDurationArrival = obj.getInt("hoursDurationArrival");
        int minutesDurationArrival = obj.getInt("minutesDurationArrival");
        int hoursDurationScale = obj.getInt("hoursDurationScale");
        int minutesDurationScale = obj.getInt("minutesDurationScale");

        return new Flight(id, plane, departureLocation, scaleLocation, arrivalLocation,
                departureDate, hoursDurationArrival, minutesDurationArrival,
                hoursDurationScale, minutesDurationScale);
    }
}
