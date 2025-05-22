package core.utils.parser.implimentations;

import core.model.entity.Location;
import core.utils.parser.interfaces.JsonParser;
import org.json.JSONObject;

public class LocationParser implements JsonParser<Location>{
    @Override
    public Location parse(JSONObject obj) {
        String airportId = obj.getString("airportId");
        String airportName = obj.getString("airportName");
        String airportCity = obj.getString("airportCity");
        String airportCountry = obj.getString("airportCountry");
        double airportLatitude = obj.getDouble("airportLatitude");
        double airportLongitude = obj.getDouble("airportLongitude");

        return new Location(airportId, airportName, airportCity, airportCountry, airportLatitude, airportLongitude);
    }
}
