package setServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JList;

import setClient.InitGame;

public class User implements Comparable<User>{
    private String userName;
    
    public Date dateRoomChange;
    public GameRoom userRoom;
    
    
    
    // Variables for the current game session
    // Check setprotocolAPI -- these stats should be sent AFTER THE END OF A GAME!
    public int games_played = 1;
    public int games_won = 0;
    public int games_lost = 0;
    public int games_tied = 0;
    Boolean forfeit = false;
    public int totalSets;
    public int correctSets;
    public int incorrectSets;
    
    public int totalScore = 0;
    
   
    public User(String name)
    {
        userName = name;
        dateRoomChange = new Date();
    }
    
    public String getName()
    {
        return userName;
    }
    
    public String toString()
    {
        if (InitGame.topIsLobby())
        {
            return userName;
        }
        else
        {
	        return userName + ": " + totalScore;
        }
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
//        totalScore = (correctSets * 6) - (totalSets * 1);
        totalScore = (correctSets * 5) - (incorrectSets * 1);
        
        if (forfeit)
        {
            totalScore = totalScore - 16;
        }
                
    }
    
    public void resetScore()
    {
	    games_played = 1;
	    games_won = 0;
	    games_lost = 0;
	    games_tied = 0;
	    forfeit = false;
	    totalSets = 0;
	    correctSets = 0;
	    incorrectSets = 0;
	    totalScore = 0;
	    
    }
    
    public Collection createScoreArray()
    {
        Collection scores = new ArrayList();
	    scores.add(games_played);
	    scores.add(games_won);
	    scores.add(games_lost);
	    scores.add(games_tied);
	   
//	    For # of games quit
	    if (forfeit)
	    {
	        scores.add(1);
	    }
	    else
	    {
	        scores.add(0);
	    }
	    
	    scores.add(totalSets);
	    scores.add(correctSets);
	    scores.add(userName);

	    return scores;
    }
    
    

    @Override
    public int compareTo(User user) {
        int lastCmp = user.dateRoomChange.compareTo(dateRoomChange);
        return lastCmp;
    }
    
}
