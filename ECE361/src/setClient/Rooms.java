package setClient;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import setServer.JSONinterface;

import com.google.gson.JsonArray;

public class Rooms {
    
    public static ArrayList<Rooms> roomList = new ArrayList<Rooms>();
    
    private String roomName;
    
    public Rooms(String rName)
    {
        roomName = rName;
    }
    
    public String toString()
    {
        return roomName;
        
    }
    
    /*
    public static void getRoomData(String roomString)
    {
        //Whether or not its ROOM or ROOMLEAVE or ROOMCREATE. grab it.
        roomString = roomString.substring(roomString.indexOf("|") + 1);
        
        String[] tmp = roomString.split("\\|");
        if (roomString.length() == 0)
        {
            roomData = null;
        }
        else
        {
            roomData = tmp;
        }
        
    }
    */

    public static void createRoomArray(String roomString)
    {
        ArrayList<String> roomData = JSONinterface.jsonGetData(roomString, ArrayList.class);
        roomList = new ArrayList<Rooms>();
        
        for (String roomName : roomData)
        {
                
            if (roomName.equals("lobby"))
            {
                continue;
            }
            roomList.add(new Rooms(roomName));
        }
        
    }
}
