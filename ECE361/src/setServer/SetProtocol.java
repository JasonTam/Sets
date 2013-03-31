package setServer;
import java.net.*;
import java.io.*;
 
public class SetProtocol {
    public static final int INITIALIZE = 0;
    public static final int LOGIN = 1;
    public static final int LOBBY = 2;
    public static final int GAME = 3;
    
    private SetProtocolAPI spAPI;
 
    public String theOutput = null;
 
    public int state = INITIALIZE;

    
    //When the setProtocol is initiated, also initiate its API
    public SetProtocol(SetMultiThread curThread) {
    	spAPI = new SetProtocolAPI(curThread, this);
    }
 
    public String processInput(String theInput, SetMultiThread curThread) {
    	
    	
        
// 		Each condition must always assign a value to theOutput.
//      Initialization of the server
    	if (state == INITIALIZE) {
    		spAPI.genericAPI.initialization();
        } 
    	else if (theInput.toLowerCase().startsWith("chat")) {
    		spAPI.genericAPI.chat(theInput);
        }
    	/*
    	else if (theInput.toLowerCase().startsWith("rooms")) {
    		spAPI.genericAPI.showRooms();
    	}
    	*/
    	else if (theInput.toLowerCase().startsWith("users")) {
    		spAPI.genericAPI.showUsers();
    	}
        else if (theInput.toLowerCase().startsWith("quit")) {
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
        else if (state == GAME) {
        	spAPI.gameAPI.gameStart(theInput);
        }

        return theOutput;
    }
}