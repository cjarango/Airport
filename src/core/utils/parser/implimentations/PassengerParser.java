package core.utils.parser.implimentations;

import core.model.entity.Passenger;
import core.utils.parser.interfaces.JsonParser;
import java.time.LocalDate;
import org.json.JSONObject;

public class PassengerParser implements JsonParser<Passenger>{
    @Override
    public Passenger parse(JSONObject obj) {
        long id = obj.getLong("id");
        String firstname = obj.getString("firstname");
        String lastname = obj.getString("lastname");
        LocalDate birthDate = LocalDate.parse(obj.getString("birthDate"));
        int countryPhoneCode = obj.getInt("countryPhoneCode");
        long phone = obj.getLong("phone");
        String country = obj.getString("country");
        return new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country);
    }

}
