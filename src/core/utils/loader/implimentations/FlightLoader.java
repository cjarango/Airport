package core.utils.loader.implimentations;

import core.model.entity.Flight;
import core.model.manager.implementations.ManagerFlight;
import core.utils.loader.interfaces.DataLoader;
import core.utils.parser.interfaces.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class FlightLoader implements DataLoader<Flight> {

    private final JsonParser<Flight> flightParser;
    private final ManagerFlight managerFlight;

    public FlightLoader(JsonParser<Flight> flightParser, ManagerFlight managerFlight) {
        this.flightParser = flightParser;
        this.managerFlight = managerFlight;
    }

    @Override
    public void loadFromFile(String filepath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray jsonArray = new JSONArray(content);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Flight flight = flightParser.parse(obj);
            managerFlight.add(flight);
        }
    }

}
