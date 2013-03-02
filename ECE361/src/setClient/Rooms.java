package setClient;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rooms {
    
    private static String[] roomData;
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

    public static void createRoomHash()
    {
        roomList = new ArrayList<Rooms>();
        
        if (roomData == null)
        {
            return;
        }
        else
        {
            for (String roomName : roomData)
            {
               String room = (roomName.split("-"))[0];
               roomList.add(new Rooms(room));
            }
        }
        
    }
}
