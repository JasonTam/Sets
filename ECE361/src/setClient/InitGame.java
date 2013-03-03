
package setClient;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import setGame.Card;
import setGame.Game;

@SuppressWarnings("serial")
public class InitGame {
    
    public static PrintWriter out;
    public static BufferedReader in;
    
    public static int PORT = 4444;
    public static String HOST = "localhost";
    public static String inputLine = null;
    
    private static Socket socket = null;
    
    private static boolean isConnected = true;
    
    public static JPanel cardLayout = new JPanel();
    public static Lobby lobbyPanel = new Lobby();
    public static GamePanel gamePanel  = new GamePanel();
    
    public static ChatPanel chatPanel = new ChatPanel();
    public static UserJList userJList = new UserJList();
    
    public static String userName = "Andrew";
    
    private static JFrame frame;
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        userJList.setPreferredSize(new Dimension(100,0));
        chatPanel.setPreferredSize(new Dimension(0, 150));
        
        //Create and set up the window.
        frame = new JFrame("SET GAME :O ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        cardLayout.setLayout(new CardLayout());
        cardLayout.add(lobbyPanel, "LOBBY");
        cardLayout.add(gamePanel, "GAME");
        
        ((CardLayout)cardLayout.getLayout()).show(cardLayout, "LOBBY");
        
        frame.add(cardLayout, BorderLayout.CENTER);
        frame.add(userJList, BorderLayout.EAST);
        frame.add(chatPanel, BorderLayout.SOUTH);
        
        //Set up the content pane.
        //frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
//        frame.pack();
        int frameWidth = 800;
        int frameHeight = 600;
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
        
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
        initServerConnection();
//        LOGIN HERE
        //Initialization- login, get users, and get rooms
        out.println("login|Andrew|andrew");
        out.println("rooms");
        out.println("users");
        
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
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
//        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                
//              Notify the server that you have quit.
		        frame.addWindowListener(new WindowAdapter() {
		            public void windowClosing(WindowEvent e) {
		                out.println("quit");
		                System.exit(0);
		            }
		        });
        }
        });
        
        try
        {
	        while ((inputLine = in.readLine()) != null)
	        {
	            System.out.println(inputLine);
	            if (inputLine.startsWith("CHAT|"))
	            {
	                lobbyPanel.chat.displayMessage(inputLine.substring(5));
	            }
//	            else if (inputLine.matches("^(ROOMS\\||ROOMCREATE\\||ROOMLEAVE\\|).*$"))
	            else if (inputLine.matches("^ROOMS\\|.*$"))
	            {
	                Rooms.getRoomData(inputLine);
	                Rooms.createRoomHash();
	                lobbyPanel.updateLobbyPanel();
	            }
	            else if (inputLine.matches("^USERS\\|.*$"))
                {
	                User.getUserData(inputLine);
	                userJList.createListModel();
                }
	            else if (inputLine.matches("^LOGIN\\|.*$"))
	            {
	                inputLine = inputLine.substring(inputLine.indexOf("|") + 1);
	                if (inputLine.equals("Andrew"))
	                {
	                    debug("same name inside");
	                    continue;
	                }
	                User.addUser(inputLine);
	                userJList.refreshJList();
	            }
	            else if (inputLine.matches("^LOGOUT\\|.*$"))
	            {
	                inputLine = inputLine.substring(inputLine.indexOf("|") + 1);
	                User.removeUser(inputLine);
	                userJList.refreshJList();
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
    }
