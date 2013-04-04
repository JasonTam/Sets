package setServer;
import java.net.*;
import java.io.*;
 
public class SetProtocol {
    public static final int INITIALIZE = 0;
    public static final int LOGIN = 1;
    public static final int LOBBY = 2;
    public static final int ROOM = 3;
    public static final int GAME = 4;
    
    private SetProtocolAPI spAPI;
 
    public String theOutput = null;
 
    public int state = INITIALIZE;

    
    //When the setProtocol is initiated, also initiate its API
    public SetProtocol(SetMultiThread curThread) {
    	spAPI = new SetProtocolAPI(curThread, this);
    }
 
    public String processInput(String theInput, SetMultiThread curThread) {
    	
    	System.out.println("The state is : " + state);
    	System.out.println("Input being processed: " + theInput);
    	String action = null;
    	try {
    		action = JSONinterface.jsonGetAction(theInput);
    	} catch (java.lang.IllegalStateException e) {
//    		Don't really care
    		System.err.println("Caught IOException: " + e.getMessage());
    	}
        
// 		Each condition must always assign a value to theOutput.
//      Initialization of the server
    	if (state == INITIALIZE) {
    		spAPI.genericAPI.initialization();
    		theOutput = JSONinterface.genericToJson("null", "sending blank line");
        } 
    	else if (action.equals("test")) {
    	    
    	    System.out.println("All Connected Threads");
    	    System.out.println(SetServer.allThreads);
    	    System.out.println("All Current Game Rooms");
    	    System.out.println(SetServer.gameRooms);
    	    System.out.println("All Usrs in your room");
    	    System.out.println(curThread.currentRoom.usersInRoom);
    	    System.out.println("All Usrs in Lobby");
    	    System.out.println(SetServer.lobby.usersInRoom.containsKey(curThread.currentUser.getName()));
    	    System.out.println(curThread.currentUser.getName());
    	    
    	    theOutput = JSONinterface.genericToJson("test", "Check Server console for hashes");
    	}
    	else if (action.equals("chat")) {
    		spAPI.genericAPI.chat(theInput);
        }
    	else if (action.equals("rooms")) {
    		spAPI.genericAPI.showRooms();
    	}
    	else if (action.equals("users")) {
    		spAPI.genericAPI.showUsers();
    	}
        else if (action.equals("quit")) {
        	spAPI.genericAPI.quitApp();
        }
//      After connecting to server, input name and validate
        else if (state == LOGIN) {
        	spAPI.loginAPI.loginStart(theInput);
//      Enter lobby after getting validated
        }
        else if (state == LOBBY) {
        	spAPI.lobbyAPI.lobbyStart(theInput);
        }
        else if (state == ROOM) {
        	spAPI.roomAPI.roomStart(theInput);
        }
        else if (state == GAME) {
        	spAPI.gameAPI.gameStart(theInput);
        }

        return theOutput;
    }
    
    public void changeState(Integer fstate, SetMultiThread curThread)
    {
        state = fstate;
        curThread.out.println(JSONinterface.genericToJson("gameState", state));
    }
}
