
package setClient;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import setGame.Card;
import setGame.Game;
import setServer.GameRoom;
import setServer.JSONinterface;
import setServer.User;

@SuppressWarnings("serial")
public class InitGame {
    
    public static PrintWriter out;
    public static BufferedReader in;
    
    public static int lineNumber = 0;
    
    public static int PORT = 4444;
//    public static String HOST = "localhost";
    public static String HOST = "199.98.20.120";
    
    
    public static String inputLine = null;
    
    private static Socket socket = null;

    private static boolean isConnected = true;
    
    public static JPanel cardLayout = new JPanel();
    public static Lobby lobbyPanel = new Lobby();
    public static GamePanel gamePanel;
    
    public static ChatPanel chatPanel = new ChatPanel();
    public static UserJList userJList = new UserJList();
    
    
    public static boolean runningFromJar = false;
    
    private static JFrame frame;
    private static Login login;
    
    public static int gameState = 0;
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    //used in Login to show lobby panel
    public static void showGame()
    {
    	frame.setVisible(true);
    }
    private static void createAndShowGUI() {
        //userJList.setPreferredSize(new Dimension(100,0));
        chatPanel.setPreferredSize(new Dimension(0, 150));
        
        //Create and set up the window.
        frame = new JFrame("SET GAME :O ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        cardLayout.setLayout(new CardLayout());
        cardLayout.add(lobbyPanel, "LOBBY");
        
        ((CardLayout)cardLayout.getLayout()).show(cardLayout, "LOBBY");
        
        frame.add(cardLayout, BorderLayout.CENTER);
        frame.add(userJList, BorderLayout.EAST);
        frame.add(chatPanel, BorderLayout.SOUTH);
        
        //Set up the content pane.
        //frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        int frameWidth = 800;
        int frameHeight = 600;
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(false);
        
    }
    
    public static void changeCards(String cardName) {
        ((CardLayout)cardLayout.getLayout()).show(cardLayout, cardName);
    }
    
    
    public static void initServerConnection()
    {
        try 
        {
	        socket = new Socket(HOST, PORT);
	           
	        out = new PrintWriter(socket.getOutputStream(), true);
	           
	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        
	        
        }
        catch (UnknownHostException e)
        {
            System.err.println("Cannot resolve host "+ HOST);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to the host "+HOST);
        }
    }
    
    public static void main(String[] args) {
        if (!InitGame.class.getResource("InitGame.class").toString().startsWith("file"))
        {
            runningFromJar = true;
            
        }
    	initServerConnection();
    	
    	
    	login = new Login();
    	login.pack();
		login.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		login.setVisible(true);
		

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {            	
                setTheme();
            	createAndShowGUI();
                
//              Notify the server that you have quit.
		        frame.addWindowListener(new WindowAdapter() {
		            public void windowClosing(WindowEvent e) {
		                out.println(JSONinterface.genericToJson("quit", "quitting game"));
		                System.exit(0);
		            }
		        });
        }
        });
        
        try
        {
//        	TODO Consider Case switch if everything will be action.equals(.)
	        while ((inputLine = in.readLine()) != null)
	        {
	            lineNumber++;
	            System.out.println("Recieved line: " + inputLine);
	        	String action = JSONinterface.jsonGetAction(inputLine);
	        	
	            if (action.equals("chat"))
	            {
	                lobbyPanel.chat.displayMessage(JSONinterface.jsonGetData(inputLine, String.class));
	            }
//	            else if (inputLine.matches("^(ROOMS\\||ROOMCREATE\\||ROOMLEAVE\\|).*$"))
	            else if (action.equals("rooms"))
	            {
				    Lobby.roomArray = JSONinterface.jsonGetData(inputLine, new TypeToken<ArrayList<GameRoom>>(){}.getType());
	                lobbyPanel.updateLobbyPanel();
	            }
	            else if (action.equals("users"))
                {
				    Lobby.userArray = JSONinterface.jsonGetData(inputLine, new TypeToken<ArrayList<User>>(){}.getType());
	                userJList.createListModel();
                }
	            else if (action.equals("create"))
	            {
	            	String message = JSONinterface.jsonGetData(inputLine, String.class);
	            	if (message.equals("fail"))
	            	{
	            		JOptionPane.showMessageDialog(frame, "Sorry, that room already exists");
	            		
	            	}
	            	else
	            	{
	            		lobbyPanel.createRoom.createRoomAction();
	            	}
	            	
	            }
	            /*
	            else if (action.equals("login"))
	            {
	            	inputLine = JSONinterface.jsonGetData(inputLine, String.class);
	            	// This following if should be USERNAME, not just a static andrew 
//	                if (inputLine.equals("Andrew"))
//	                {
//	                    debug("same name inside");
//	                    continue;
//	                }
//	                userJList.refreshJList();
	            }
	            else if (action.equals("logout"))
	            {
	                userJList.refreshJList();
	            }
	            */
	            else if (action.equals("startGame"))
	            {
	                GamePanel.curGame = JSONinterface.jsonGetData(inputLine, Game.class);
	                gamePanel.setupGame();
	                
	            }
	            else if (action.equals("updateGame"))
	            {
//	            	TODO
//	            	May want to only send deltas rather than entire game
//	            	This is temporary?
	                GamePanel.curGame = JSONinterface.jsonGetData(inputLine, Game.class);
                    gamePanel.updateGame();
	                
	            }
	            else if (action.equals("gameResults"))
	            {
	                
	            	ArrayList<User> scoreData = JSONinterface.jsonGetData(inputLine, new TypeToken<ArrayList<User>>(){}.getType());
	            	debug(scoreData);

	            	gamePanel.clearButton.setEnabled(false);
	            	gamePanel.submitButton.setEnabled(false);
	            	ArrayList<User> scoreDataWithScoreOrder=scoreData;
	            	int[] scorekeeper = new int[scoreData.size()];
	            	int index=0;
	            	for( User usr2: scoreDataWithScoreOrder)
	            	{
	            		int higher=0;
	            		for(User usr:scoreData)
	            		{
	            			if(usr2.getTotalScore()>usr.getTotalScore())
	            			{
	            				higher++;
	            			}
	            		}
	            		scorekeeper[index]=higher;
	            		index++;
	            	}
	            	//
	            	User tmp2;
	            	int tmp;
	            	for (int k=0; k<scoreData.size()-1; k++) {

	            	    boolean isSorted=true;
	            	    for (int i=1; i<scoreData.size()-k; i++) {

	            	         if (scorekeeper[i]>scorekeeper[i-1]  ) {
	            	             tmp=scorekeeper[i];
	            	             scorekeeper[i]=scorekeeper[i-1];
	            	             scorekeeper[i-1]=tmp;


	            	            tmp2=scoreData.get(i);
	            	            scoreData.set(i,scoreData.get(i-1));
	            	            scoreData.set(i-1,tmp2);

	            	            isSorted=false;
	            	        }
	            	    }
	            	    if (isSorted) break;
	            	}

	            	//

	            	String data="";
	            	String winnerNames="";
	            	int winnerCounter = 0;
	            	int highScore=scoreData.get(0).getTotalScore();
	            	for( User usr: scoreData)
	            	{   
	            		if(usr.getTotalScore()<highScore)
	            		{
	            		data += "<tr>";
	            		data += "<td>"+usr.getName()+"</td>";
	            		data += "<td>"+usr.getTotalScore()+"</td>";
	            		data += "</tr>";
	            		}else{
	            			winnerNames += " " + usr.getName();
	            			data += "<tr>";
	            	    	data += "<td>"+usr.getName()+"</td>";
	            	    	data += "<td>"+usr.getTotalScore()+"</td>";
	            	    	data += "</tr>";
	            	    	winnerCounter++;
	            		}
	            	}

	            	if (winnerCounter == 1)
	            	{
		            	JOptionPane.showMessageDialog(frame,
		            	    "<html>" +
		            	    	"<h3> The Winner is" + winnerNames +"!!!</h3>"+	                              
		            	        "<table>"+data+"</table>"+	                     
		            	    "</html>"
		            	);
	            	}
	            	else
	            	{
		            	JOptionPane.showMessageDialog(frame,
		            	    "<html>" +
		            	    	"<h3> There is a tie between:" + winnerNames +"!!!</h3>"+	                              
		            	        "<table>"+data+"</table>"+	                     
		            	    "</html>"
		            	);
	            	    
	            	}
                
	                
	            }
	            else if (action.equals("gameState"))
	            {
	                gameState = JSONinterface.jsonGetData(inputLine, Integer.class);
	            }
	            else if (action.equals("login-response"))
	            {
	                if (JSONinterface.jsonGetData(inputLine, String.class).equals("badInfo"))
	                {
	                    login.jlbOutMessage.setText("Invalid login information.");
	                    
	                }
	                else if (JSONinterface.jsonGetData(inputLine, String.class).equals("alreadyOn"))
	                {
	                    login.jlbOutMessage.setText("Already logged on.");
	                    
	                }
	                else if (JSONinterface.jsonGetData(inputLine, String.class).equals("good"))
	                {
				    	InitGame.out.println(JSONinterface.genericToJson("users", "show all users"));
				    	InitGame.out.println(JSONinterface.genericToJson("rooms", "show all rooms"));
						login.setVisible(false);
						Lobby.userName = login.jtfInput.getText();
						InitGame.showGame();
	                }
	                
	            }
	            else {
	                debug("Nothing done");
	            }
	            
	        }
	        debug("Done");
        }
        catch (IOException e) 
        {
            e.printStackTrace(); 
        }
        }
    
        public static void debug(Object msg)
        {
           System.out.println(msg);
        }
        
        public static Boolean topIsLobby()
        {
            for (Component comp : cardLayout.getComponents() ) {
                if (comp.isVisible() == true && comp.getClass().equals(Lobby.class)) {
                    return true;
                }
            }
            return false;
        }
        
        public static void setTheme() {
        	/* Use an appropriate Look and Feel */
            try {
                //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            /* Turn off metal's use of bold fonts */
//            UIManager.put("swing.boldMetal", Boolean.FALSE);
        }
    }
