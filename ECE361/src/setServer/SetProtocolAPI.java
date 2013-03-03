package setServer;

import java.util.Iterator;

import DBI.DBConnect;

public class SetProtocolAPI {
		private SetMultiThread curThread;
		private SetProtocol sp;
		
		public login loginAPI;
		public game gameAPI;
		public lobby lobbyAPI;
		public generic genericAPI;
		
		public DBConnect dbc;
		
		public SetProtocolAPI outer = SetProtocolAPI.this;
		
		public SetProtocolAPI(SetMultiThread spThread, SetProtocol sp) {
			this.curThread = spThread;
			this.sp = sp;

			this.loginAPI = new login();
			this.gameAPI = new game();
			this.lobbyAPI = new lobby();
			this.genericAPI = new generic();
			this.dbc = spThread.dbc;
		}
		
		public String[] splitString(String theInput) {
			String[] stringArray = theInput.split("\\|");
			
			return stringArray;
		}
		
		public class generic {
			public void initialization() {
	        	//sp.theOutput = "Please enter a valid username.";
	    		sp.state = SetProtocol.LOGIN;
			}
			
			public void chat(String theInput) {
				String message = theInput.toLowerCase().substring(5);
				SetServer.sendChat(curThread.currentRoom.getName(), curThread, message);
				sp.theOutput = "You just broadcasted a message";
			}
			
			public void showRooms() {
			    SetServer.sendRooms(sp);
			}
			
			public void showUsers() {
			    SetServer.sendUsers(sp);
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
			    SetServer.sendLogout(curThread.getName(), sp);
	        	sp.theOutput = "Bye.";
			}
			
		}
		
		public class login {
			
			public void loginStart (String theInput) {
	        	if (theInput.toLowerCase().startsWith("login")) {
	        		validateUser(theInput);
	        	}
	        	else {
	        		invalidUser(theInput);
	        	}
			}
			
			private void validateUser(String theInput) {
        		String [] userInfo = outer.splitString(theInput);
        		String username = userInfo[1];
        		String password = userInfo[2];
				
        		/* SKIP THIS FOR TESTING PURPOSES!!!
//        		First of all, check if anyone else with your acct is logged in:
        		if (SetServer.allThreads.containsKey(username)) {
        			sp.theOutput = "error|fail";
        			System.out.println("Already logged in");
        			return;
        		}
        		*/
        		
//	        	Once you get logged in, you autommatically are entered into the lobby.
				try {
	        		if (dbc.validateUser(username, password)) {
	        			curThread.setName(username);
	        			
	        			SetServer.allThreads.remove(Integer.toString(curThread.default_name));
	        			SetServer.allThreads.put(curThread.getName(), curThread);
	        			SetServer.lobby.join(curThread);
	        			
	        			SetServer.sendLogin(curThread.getName(), sp);
	        			
	        			sp.state = SetProtocol.LOBBY;
	        		}
	        		else {
	        			sp.theOutput = "Bad login information";
	        		}
        		}
        		catch (Exception e){
        			e.printStackTrace();
        		}
				
				
			}
			
			private void invalidUser(String theInput) {
				sp.theOutput = theInput;
			}
			
		}
		
		
		public class lobby {
			
			public void lobbyStart (String theInput) {	

//		        Create / join a set room
		        if (theInput.toLowerCase().startsWith("join")) {
		        	joinGame(theInput);
		    	}

		        else if (theInput.toLowerCase().startsWith("set")) {
		        	//theOutput = gameLogic.set();
		        }
		        else {
		        	lobbyInvalid(theInput);
		        }
			}
			
			private void lobbyInvalid(String theInput) {
				sp.theOutput = "In Lobby";
			}
			
			private void joinGame(String theInput) {
				String [] roomInfo = outer.splitString(theInput);
				String roomName = roomInfo[1];
	        	
//	        	If a user tries to enter a room (that is not the lobby) and its full, don't let them in!
	        	if (!roomName.equals(SetServer.lobby.getName()) && SetServer.gameRooms.containsKey(roomName) && SetServer.gameRooms.get(roomName).size() == 2) {
        			sp.theOutput = "error|fail";
        		}
//	        	If the room doesn't exist, create it and join the room
	        	else if (!SetServer.gameRooms.containsKey(roomName)) {
	        	    SetServer.sendRoomCreate(roomName, curThread, sp);
	        	}
//	        	If the room does exist and its not full, join it
	        	else {
	        		SetServer.gameRooms.get(roomName).join(curThread);
	        		sp.theOutput = "success|join";
	        		sp.state = SetProtocol.GAME;
	        	}
			}
		}

		public class game {
			
			public void gameStart(String theInput) {
				if (theInput.toLowerCase().startsWith("leave")) {
//				    System.out.println(curThread.currentRoom);
//					leaveGame();
				    SetServer.sendRoomLeave(curThread, sp);
				}
				else {
					gameInvalid();
				}
			}
			
			private void leaveGame() {
				sp.theOutput = "ROOMLEAVE|" + curThread.currentRoom;
				curThread.currentRoom.leave(curThread);
				SetServer.lobby.join(curThread);
				sp.state = SetProtocol.LOBBY;
			}
			
			private void gameInvalid() {
				sp.theOutput = "In Game";
			}
		}
}
