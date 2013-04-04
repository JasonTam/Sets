package setServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JList;

import setClient.InitGame;

public class User implements Comparable<User>{
    private String userName;
    public static LinkedHashMap<String, User> userList = new LinkedHashMap();
    private static String[] userData;
    
    public Date dateRoomChange;
    
    // Probably should pull stats from the database and give it the user.
    public User(String name)
    {
        userName = name;
        dateRoomChange = new Date();
        
        addUser();
    }
    
    public String toString()
    {
        return userName;
    }
    
    /*
    public static void getUserData(String inputLine)
    {
        InitGame.debug("User.getUserData");
        inputLine = inputLine.substring(inputLine.indexOf("|") + 1);
        String[] tmp = inputLine.split("\\|");
        
        if (inputLine.length() == 0)
        {
            userData = null;
        }
        else
        {
            userData = tmp;
        }
        createUserList();
        InitGame.debug("User.getUserData");
    }
    */
    
    
    
    public static ArrayList<User> getUsersInRoomArray(GameRoom room)
    {
        ArrayList<User> usersArray = new ArrayList<User>();
	    Iterator<String> itUsers = room.usersInRoom.keySet().iterator();
	    
	    while (itUsers.hasNext())
	    {
	        String key = (String) itUsers.next();
	        User curUser = room.usersInRoom.get(key);
	        
	        usersArray.add(curUser);
	    }
	    
	    //Sort the rooms by date...
	    Collections.sort(usersArray);
	    
	    return usersArray;
    }
    
    private void addUser()
    {
        InitGame.debug("User.addUser");
        userList.put(userName, this);
        InitGame.debug("User.addUser");
        
    }
    
    public static void removeUser(String userName)
    {
        userList.remove(userName);
        User removed = userList.get(userName);
        removed = null;
    }

    @Override
    public int compareTo(User user) {
        int lastCmp = user.dateRoomChange.compareTo(dateRoomChange);
        return lastCmp;
    }
    
}