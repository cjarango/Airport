package core.utils.parser.interfaces;

public interface JsonParser<T> {
    T parse(org.json.JSONObject obj);
}
