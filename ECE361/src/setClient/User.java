package setClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JList;

public class User {
    private static String userName;
    public static LinkedHashMap<String, User> userList = new LinkedHashMap();
    private static String[] userData;
    public static JList userJList;
    
    public User(String name)
    {
        userName = name;
    }
    
    public String toString()
    {
        return User.userName;
    }
    
    
    public static void getUserData(String inputLine)
    {
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
    }
    
    public static void createUserList()
    {
        for (String person : userData)
        {
            addUser(person);
        }
    }
    
    public static void addUser(String userName)
    {
        User user = new User(userName);
        userList.put(userName, user);
        
    }
    
    public static void removeUser(String userName)
    {
        userList.remove(userName);
        User removed = userList.get(userName);
        removed = null;
    }
    
}
