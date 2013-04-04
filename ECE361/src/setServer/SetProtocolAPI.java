package setServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;




import setGame.Card;
import setGame.Game;
import setGame.GameLogic;

import DBI.DBConnect;

public class SetProtocolAPI {
		private SetMultiThread curThread;
		private SetProtocol sp;
		
		public login loginAPI;
		public game gameAPI;
		public lobby lobbyAPI;
		public generic genericAPI;
		public room roomAPI;
		
		public DBConnect dbc;
		
		public SetProtocolAPI outer = SetProtocolAPI.this;
		
		public SetProtocolAPI(SetMultiThread spThread, SetProtocol sp) {
			this.curThread = spThread;
			this.sp = sp;

			this.loginAPI = new login();
			this.gameAPI = new game();
			this.lobbyAPI = new lobby();
			this.genericAPI = new generic();
			this.roomAPI = new room();
			this.dbc = spThread.dbc;
		}
		
		public String[] splitString(String theInput) {
			String[] stringArray = theInput.split("\\|");
			
			return stringArray;
		}
		
		public class generic {
			public void initialization() {
	        	//sp.theOutput = "Please enter a valid username.";
			    sp.changeState(SetProtocol.LOGIN, curThread);
			}
			
			public void chat(String theInput) {
				String message = JSONinterface.jsonGetData(theInput, String.class);
				SetServer.sendChat(curThread.currentRoom.getName(), curThread, message,1);
				sp.theOutput = JSONinterface.genericToJson("null", "broadcasting chat");
			}
			
			public void showRooms() {
			    SetServer.sendRooms(sp);
			    sp.theOutput = JSONinterface.genericToJson("null", "broadcasting rooms");
			}
			
			public void showUsers() {
			    SetServer.sendRoomUsers();
			    sp.theOutput = JSONinterface.genericToJson("null", "sending users in room user list.");
			    /*
				sp.theOutput = "USERS|";
			    Iterator itUsers = SetServer.allThreads.keySet().iterator();
			    while (itUsers.hasNext()) {	
			        String user = (String) itUsers.next();
			        sp.theOutput = sp.theOutput.concat(user + "|");
				    //			        it.remove(); // avoids a ConcurrentModificationException
			    }
			    */
		    }
			
			public void quitApp() {
//			    SetServer.sendLogout(curThread.getName(), sp);
			    SetServer.sendRoomUsers();
	        	sp.theOutput = "Bye.";
			}
			
		}
		
		public class login {
			
			public void loginStart (String theInput) {
				String action = JSONinterface.jsonGetAction(theInput);
				
	        	if (action.equals("login")) {
	        		validateUser(theInput);
	        	}
	        	else {
	        		invalidUser(theInput);
	        	}
			}
			
			private void validateUser(String theInput) {
				ArrayList<String> data = JSONinterface.jsonGetData(theInput, ArrayList.class);
        		String username = data.get(0);
        		String password = data.get(1);
				
//        		First of all, check if anyone else with your acct is logged in:
        		if (SetServer.allThreads.containsKey(username)) {
        			sp.theOutput = JSONinterface.genericToJson("login-response", "alreadyOn");
        			System.out.println("Already logged in");
        			return;
        		}
        		
//	        	Once you get logged in, you autommatically are entered into the lobby.
				try {
	        		if (dbc.validateUser(username, password)) {
	        			curThread.setName(username);
	        			
	        			curThread.currentUser = new User(username);
	        			
	        			SetServer.lobby.join(curThread);
	        			SetServer.allThreads.put(curThread.getName(), curThread);
	        			
//	        			SetServer.sendLogin(curThread.getName(), sp);
	        			SetServer.sendRoomUsers();
	        			sp.changeState(SetProtocol.LOBBY, curThread);
	        			sp.theOutput = JSONinterface.genericToJson("login-response", "good");
	        		}
	        		else {
	        			sp.theOutput = JSONinterface.genericToJson("login-response", "badInfo");
	        		}
        		}
        		catch (Exception e){
        			e.printStackTrace();
        		}
				
				
			}
			
			private void invalidUser(String theInput) {
			    sp.theOutput = JSONinterface.genericToJson("condition", "bad login");
			}
			
		}
		
		
		public class lobby {
			
			public void lobbyStart (String theInput) {	
			    String action = JSONinterface.jsonGetAction(theInput);

//		        Create / join a set room
		        if (action.equals("join")) {
		        	joinRoom(theInput);
		    	}

		        else if (theInput.toLowerCase().startsWith("set")) {
		        	//theOutput = gameLogic.set();
		        }
		        else {
		        	lobbyInvalid(theInput);
		        }
			}
			
			private void lobbyInvalid(String theInput) {
				sp.theOutput = JSONinterface.genericToJson("null", "Invalid Lobby command");
			}
			
			private void joinRoom(String theInput) {
				String roomName = JSONinterface.jsonGetData(theInput, String.class);
				
//	        	If a user tries to enter a room (that is not the lobby) and its full, don't let them in!
	        	if (!roomName.equals(SetServer.lobby.getName()) && SetServer.gameRooms.containsKey(roomName) && SetServer.gameRooms.get(roomName).threadsInRoom.size() >= 2) {
	        	    
        			sp.theOutput = JSONinterface.genericToJson("joinError", "fullroom");
        		}
//	        	If the room doesn't exist, create it and join the room
	        	else if (!SetServer.gameRooms.containsKey(roomName)) {
	        	    new GameRoom(roomName, curThread);
	        	    sp.changeState(SetProtocol.ROOM, curThread);
//	        	    SetServer.sendRoomCreate(roomName, curThread, sp);
	        	    SetServer.sendRooms(sp);
	        		SetServer.sendRoomUsers();
	        	}
//	        	If the room does exist and its not full, join it
	        	else {
	        		SetServer.gameRooms.get(roomName).join(curThread);
	        		SetServer.sendRoomUsers();
	        		sp.changeState(SetProtocol.ROOM, curThread);
	        	}
			}
		}
		
		public class room {
			
			public void roomStart(String theInput) {
				
				String action = JSONinterface.jsonGetAction(theInput);

	        	if (action.equals("leave")) {
//				    SetServer.sendRoomLeave(curThread, sp);
	        	    SetServer.lobby.join(curThread);
	        	    sp.changeState(SetProtocol.LOBBY, curThread);
	        	    SetServer.sendRooms(sp);
	        	    SetServer.sendRoomUsers();
	        	}
	        	else if (action.equals("startGame"))
	        	{
	        	    String roomName = JSONinterface.jsonGetData(theInput, String.class);
	        	    String JSON = JSONinterface.genericToJson("startGame", SetServer.gameRooms.get(roomName).startGame());
	        	    ArrayList<SetMultiThread> roomThreads = SetServer.getRoomThreads(curThread);
	        	    
	        	    for (SetMultiThread thread : roomThreads)
	        	    {
	        	        thread.out.println(JSON);
	        	        thread.sp.state = SetProtocol.GAME;
	        	    }
	        	    
	        	}
	        	else {
	        		roomInvalid();
	        	}
			}
	        	
			private void roomInvalid() {
				sp.theOutput = JSONinterface.genericToJson("null", "Not a valid room command");
			}
	        	
		}

		public class game {
			
			public void gameStart(String theInput) {
				
				String action = JSONinterface.jsonGetAction(theInput);
				
	        	if (action.equals("submit")) {
	        		System.out.println("Recieved a submit action");
	        		ArrayList<SetMultiThread> roomThreads = SetServer.getRoomThreads(curThread);
	        		
	        		java.lang.reflect.Type collectionType = new com.google.gson.reflect.TypeToken<Collection<Card>>(){}.getType();
	        		Collection<Card> selectedCards =
	        				JSONinterface.jsonGetData(theInput, collectionType);
	        		GameRoom curGameRoom = SetServer.gameRooms.get(curThread.currentRoom.getName());
	        		boolean isSet = curGameRoom.getCurGame().submitSet(selectedCards);
	        		
//	        		boolean isSet = GameLogic.isSet(selectedCards);
	        		System.out.println("SERVER SAYS SET IS : " + isSet);
	        		
	        		
	        		sp.theOutput = JSONinterface.genericToJson("isSet", isSet);
	        		
	        		curGameRoom.getCurGame().print();
	        		System.out.println("IS GAME OVER?");
	        		System.out.println(curGameRoom.getCurGame().isGameOver());
	        		if (isSet) {
	        		    // This is how you send to all threads in a room
		        		for (SetMultiThread thread : roomThreads)
		        		{
		        	        thread.out.println(JSONinterface.genericToJson("updateGame", curGameRoom.getCurGame()));
		        		}
//		            	TODO
//		            	May want to only send deltas rather than entire game
//	        			TODO
//	        			this might trigger other events
	        		}
	        		
	        	}
	        	else if(action.equals("leave")){
	        		System.out.println("Recieved a LEAVE action");
	        		String roomName = JSONinterface.jsonGetData(theInput, String.class);
	        		System.out.println(roomName);
//		        	    String JSON = JSONinterface.genericToJson("startGame", SetServer.gameRooms.get(roomName).startGame());
		        	    ArrayList<SetMultiThread> roomThreads = SetServer.getRoomThreads(curThread);
		        	    //ArrayList<SetMultiThread> usersThreads= SetServer.ge
		        	    //int si=roomThreads.size();
		        	String message = "has left the room";
		        	    for (SetMultiThread thread : roomThreads)
		        	    {
		        	    	if(!thread.equals(curThread))
		        	    	{
		        	    	SetServer.sendChat(roomName, curThread, message,2);
		        	    	}
		        	    }
	        		    
//		        	    SetServer.sendRoomLeave(curThread, sp);
		        	    SetServer.lobby.join(curThread);
		        	    sp.changeState(SetProtocol.LOBBY, curThread);
		        	    SetServer.sendRooms(sp);
		        	    SetServer.sendRoomUsers();
	        	}
	        	else {
	        		gameInvalid();
	        	}
	        	
			}
			
			
			/* This is a command the server has to send everyone.
			private void leaveGame() {
				sp.theOutput = "ROOMLEAVE|" + curThread.currentRoom;
				curThread.currentRoom.leave(curThread);
				SetServer.lobby.join(curThread);
				sp.state = SetProtocol.LOBBY;
			}
			*/
			
			private void gameInvalid() {
				sp.theOutput = JSONinterface.genericToJson("null", "Invalid game state command");
				
			}
		}
}
