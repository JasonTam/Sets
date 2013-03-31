package setServer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import setGame.Game;

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
	
	public static <T> String genericToJson(String action, T data, Type clazz) {
		Collection col = new ArrayList();
		col.add(action);
		col.add(data);
		Gson gson = new Gson();
		return gson.toJson(col, (Type) clazz);
	}
	
	public static String jsonGetAction (String json) {
		JsonParser parser = new JsonParser();
	    JsonArray array = parser.parse(json).getAsJsonArray();
	    Gson gson = new Gson();
	    String action = gson.fromJson(array.get(0), String.class);
	    return action;
	}
	
//	Example use case:
//	int dat = jsonGetData(myJsonString, int.class);
	public static <T> T jsonGetData (String json, Class<T> clazz) {
		JsonParser parser = new JsonParser();
	    JsonArray array = parser.parse(json).getAsJsonArray();
	    Gson gson = new Gson();
	    
	    
	    T data = gson.fromJson(array.get(1), clazz);
		return data;
	}
	
	public static <T> T jsonGetData (String json, Type clazz) {
		JsonParser parser = new JsonParser();
	    JsonArray array = parser.parse(json).getAsJsonArray();
	    Gson gson = new Gson();
	    
	    
	    T data = gson.fromJson(array.get(1), clazz);
		return data;
	}
	
//	public static Collection jsonGetData (String json) {
//		JsonParser parser = new JsonParser();
//	    JsonArray array = parser.parse(json).getAsJsonArray();
//	    Gson gson = new Gson();
//	    Collection data = (ArrayList<Card>)gson.fromJson(array.get(1), ArrayList.class);
//		return data;
//	}
	
    
}
