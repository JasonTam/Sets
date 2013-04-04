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
    
    public Date dateRoomChange;
    
    
    
    // Variables for the current game session
    // Check setprotocolAPI -- these stats should be sent AFTER THE END OF A GAME!
    public int correctSets;
    public int totalSets;
    Boolean forfeit = false;
    public int totalScore = 0;
    
   
    public User(String name)
    {
        userName = name;
        dateRoomChange = new Date();
    }
    
    public String toString()
    {
        return userName + ": " + totalScore;
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
    
    //Every time you guess, you lose a point
    //Every time you guess correctly, you gain 6 points.
    // Everytime you forfeit a game, you lose 16 points.
    public void updateScore()
    {
        totalScore = (correctSets * 6) - (totalSets * 1);
        
        if (forfeit)
        {
            totalScore = totalScore - 16;
        }
                
    }
    
    

    @Override
    public int compareTo(User user) {
        int lastCmp = user.dateRoomChange.compareTo(dateRoomChange);
        return lastCmp;
    }
    
}
