package setClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JList;

import setServer.JSONinterface;

public class User {
    private String userName;
    public static LinkedHashMap<String, User> userList = new LinkedHashMap();
    private static String[] userData;
    
    public User(String name)
    {
        userName = name;
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
    
    // Esentially gets called ONLY when the current user logs on.
    public static void createUserList(String theInput)
    {
        InitGame.debug("User.createUserList");
        ArrayList<String> userListData = JSONinterface.jsonGetData(theInput, ArrayList.class);
        for (String person : userListData)
        {
            addUser(person);
        }
        InitGame.debug("User.createUserList");
    }
    
    // Called when a new user logs on
    public static void addUser(String userName)
    {
        InitGame.debug("User.addUser");
        User user = new User(userName);
        userList.put(userName, user);
        InitGame.debug("User.addUser");
        
    }
    
    public static void removeUser(String userName)
    {
        userList.remove(userName);
        User removed = userList.get(userName);
        removed = null;
    }
    
}
