package setGame;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.*;

public class JSONinterface {

	public static void getJSON(Game game) {
		
	}
	
	public static <T> String genericToJson(String action, T data) {
		Collection col = new ArrayList();
		col.add(action);
		col.add(data);
		Gson gson = new Gson();
		return gson.toJson(col);
	}
	
	public static String jsonGetAction (String json) {
		JsonParser parser = new JsonParser();
	    JsonArray array = parser.parse(json).getAsJsonArray();
	    Gson gson = new Gson();
	    String action = gson.fromJson(array.get(0), String.class);
	    return action;
	}
	
	
	
//	public static Collection jsonGetData (String json) {
//		JsonParser parser = new JsonParser();
//	    JsonArray array = parser.parse(json).getAsJsonArray();
//	    Gson gson = new Gson();
//	    Collection data = (ArrayList<Card>)gson.fromJson(array.get(1), ArrayList.class);
//		return data;
//	}
	
    
}
