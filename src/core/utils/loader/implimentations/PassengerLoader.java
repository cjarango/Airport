
package core.utils.loader.implimentations;

import core.model.entity.Passenger;
import core.model.manager.implementations.ManagerPassenger;
import core.utils.loader.interfaces.DataLoader;
import core.utils.parser.interfaces.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class PassengerLoader implements DataLoader<Passenger> {
    private final JsonParser<Passenger> passengerParser;
    private final ManagerPassenger managerPassenger;

    public PassengerLoader(JsonParser<Passenger> passengerParser, ManagerPassenger managerPassenger) {
        this.passengerParser = passengerParser;
        this.managerPassenger = managerPassenger;
    }

    @Override
    public void loadFromFile(String filepath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray jsonArray = new JSONArray(content);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Passenger passenger = passengerParser.parse(obj);
            managerPassenger.add(passenger);
        }
    }
    
}
