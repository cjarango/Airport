package core.utils.loader.implimentations;

import core.model.entity.Plane;
import core.model.manager.implementations.ManagerPlane;
import core.utils.loader.interfaces.DataLoader;
import core.utils.parser.interfaces.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlaneLoader implements DataLoader<Plane> {

    private final JsonParser<Plane> planeParser;
    private final ManagerPlane managerPlane;

    public PlaneLoader(JsonParser<Plane> planeParser, ManagerPlane managerPlane) {
        this.planeParser = planeParser;
        this.managerPlane = managerPlane;
    }

    @Override
    public void loadFromFile(String filepath) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(filepath)));
        JSONArray jsonArray = new JSONArray(content);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Plane plane = planeParser.parse(obj);
            managerPlane.add(plane);
        }
    }
}
