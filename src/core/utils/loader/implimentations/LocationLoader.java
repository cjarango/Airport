package core.utils.loader.implimentations;

import core.model.entity.Location;
import core.model.manager.implementations.ManagerLocation;
import core.utils.loader.interfaces.DataLoader;
import core.utils.parser.interfaces.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationLoader implements DataLoader<Location> {

    private final JsonParser<Location> locationParser;
    private final ManagerLocation managerLocation;

    public LocationLoader(JsonParser<Location> locationParser, ManagerLocation managerLocation) {
        this.locationParser = locationParser;
        this.managerLocation = managerLocation;
    }

    @Override
    public void loadFromFile(String filepath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray jsonArray = new JSONArray(content);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Location location = locationParser.parse(obj);
            managerLocation.add(location);
        }
    }

}
