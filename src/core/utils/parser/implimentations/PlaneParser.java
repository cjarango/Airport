package core.utils.parser.implimentations;

import core.model.entity.Plane;
import core.utils.parser.interfaces.JsonParser;
import org.json.JSONObject;

public class PlaneParser implements JsonParser <Plane>{

    @Override
    public Plane parse(JSONObject obj) {
        String id = obj.getString("id");
        String brand = obj.getString("brand");
        String model = obj.getString("model");
        int maxCapacity = obj.getInt("maxCapacity");
        String airline = obj.getString("airline");
        return new Plane(id, brand, model, maxCapacity, airline);
    }
}
