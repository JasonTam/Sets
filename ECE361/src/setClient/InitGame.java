
package setClient;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    
    public static ChatPanel chatPanel  = new ChatPanel();
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        
        //Create and set up the window.
        JFrame frame = new JFrame("SET GAME :O ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        frame.add(chatPanel, BorderLayout.CENTER);
        
        //Set up the content pane.
        //frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
//        frame.pack();
        int frameWidth = 800;
        int frameHeight = 600;
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
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
        }
        });
        initServerConnection();
//        LOGIN HERE
        out.println("login|Andrew|andrew");
        try
        {
	        while ((inputLine = in.readLine()) != null)
	        {
	            if (inputLine.startsWith("CHAT|")) {
	                chatPanel.displayMessage(inputLine.substring(5));
	            }
	            else {
	                System.out.println(inputLine);
	            }
	        }
	        System.out.println("Done");
        }
        catch (IOException e) 
        {
            e.printStackTrace(); 
        }
        }
    }