package setClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JList;

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
    
    public static void createUserList()
    {
        InitGame.debug("User.createUserList");
        for (String person : userData)
        {
            addUser(person);
        }
        InitGame.debug("User.createUserList");
    }
    
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
