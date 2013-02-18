package setServer;

public class SetProtocolAPI {
		private SetMultiThread curThread;
		private SetProtocol sp;
		
		public login loginAPI;
		public game gameAPI;
		public lobby lobbyAPI;
		public generic genericAPI;
		
		public SetProtocolAPI(SetMultiThread spThread, SetProtocol sp) {
			this.curThread = spThread;
			this.sp = sp;

			this.loginAPI = new login();
			this.gameAPI = new game();
			this.lobbyAPI = new lobby();
			this.genericAPI = new generic();
		}
		
		public class generic {
			public void initialization() {
	        	sp.theOutput = "Please enter a valid username.";
	    		sp.state = SetProtocol.LOGIN;
			}
			
			public void chat(String theInput) {
				String message = theInput.toLowerCase().substring(5);
				SetServer.broadcast(curThread.currentRoom.getName(), curThread, message);
				sp.theOutput = "You just broadcasted a message";
			}
			
			public void showRooms() {
	        	System.out.println(SetServer.gameRooms);
	        	sp.theOutput = "hsowed rooms";
			}
			
			public void showUsers() {
	        	System.out.println(SetServer.allThreads);
	        	sp.theOutput = "showed users";
			}
			
			public void quitApp() {
	        	sp.theOutput = "Bye.";
			}
			
		}
		
		public class login {
			
			public void loginStart (String theInput) {
	        	if (theInput.toLowerCase().startsWith("name")) {
	        		validateUser(theInput);
	        	}
	        	else {
	        		invalidUser(theInput);
	        	}
			}
			
			private void validateUser(String theInput) {
				curThread.setName(theInput.substring(5, theInput.length()));
//	        	Once you get logged in, you autommatically are entered into the lobby.
				SetServer.lobby.join(curThread);
				sp.theOutput = "Entering Lobby";
				sp.state = SetProtocol.LOBBY;
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
				sp.theOutput = "InvalidResponse";
			}
			
			private void joinGame(String theInput) {
	        	String roomName = theInput.toLowerCase().substring(5);
	        	
//	        	If a user tries to enter a room (that is not the lobby) and its full, don't let them in!
	        	if (!roomName.equals(SetServer.lobby.getName()) && SetServer.gameRooms.containsKey(roomName) && SetServer.gameRooms.get(roomName).size() == 2) {
        			sp.theOutput = "This room is full!";
        		}
//	        	If the room doesn't exist, create it and join the room
	        	else if (!SetServer.gameRooms.containsKey(roomName)) {
	        		new GameRoom(roomName, curThread);
	        		sp.theOutput = "Created and Joined room";
	        		sp.state = SetProtocol.GAME;
	        	}
//	        	If the room does exist and its not full, join it
	        	else {
	        		SetServer.gameRooms.get(roomName).join(curThread);
	        		sp.theOutput = "Joined existing room";
	        		sp.state = SetProtocol.GAME;
	        	}
			}
		}

		public class game {
			
			public void gameStart(String theInput) {
				if (theInput.toLowerCase().startsWith("leave")) {
					leaveGame();
				}
				else {
					gameInvalid();
				}
			}
			
			private void leaveGame() {
				curThread.currentRoom.leave(curThread);
				SetServer.lobby.join(curThread);
				sp.theOutput = "Leaving Game";
				sp.state = SetProtocol.LOBBY;
			}
			
			private void gameInvalid() {
				sp.theOutput = "Entering Game";
			}
		}
		


}
