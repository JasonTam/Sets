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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import setGame.Card;
import setGame.Game;

// TODO 
// Need to go through game flow 
// Need event listeners to connect to state of the game

@SuppressWarnings("serial")
public class GameGridTest extends JFrame {
    final static int maxGap = 100;
    
    
    public static PrintWriter out;
    public static BufferedReader in;
    
//    public static int PORT = 4444;
//    public static String HOST = "localhost";
//    public static String inputLine = null;
//    
//    private static Socket socket = null;
//    
//    private static boolean isConnected = true;
    
    JButton submitButton = new JButton("Submit Set");
    JButton clearButton = new JButton("Clear Selection");
    GridLayout experimentLayout = new GridLayout(3,5);
    
    public GameGridTest(String name) {
        super(name);
        setResizable(false);
    }
    
    public void addComponentsToPane(final Container pane) {
        final JPanel compsToExperiment = new JPanel();
        compsToExperiment.setLayout(experimentLayout);
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(2,3));
        
        //Set up components preferred size
        JButton b = new JButton("Just fake button");
        Dimension buttonSize = b.getPreferredSize();
        compsToExperiment.setPreferredSize(new Dimension(
        		(int)(buttonSize.getWidth() * 2.5)+maxGap,
                (int)(buttonSize.getHeight() * 3.5)+maxGap * 2));
        
        final Map<String, JToggleButton> cardButtons = new HashMap<String, JToggleButton>();
        final Collection<Card> selectedCards = new HashSet<Card>();
        //Add buttons to experiment with Grid Layout      
        for (int i = 0; i<Game.getIndex(); i++){
        	final Card c = Game.getDeck().get(i);
//			System.out.println(c.getStrId());
			ImageIcon card_img = new ImageIcon
                    ("src/resources/images_cards/"+c.toString()+".gif");
			final JToggleButton bC = new JToggleButton(card_img);
//			final JToggleButton bC = new JToggleButton(c.toString());
			compsToExperiment.add(bC);
			cardButtons.put(c.toString(), bC);
			bC.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(bC.isSelected()){
	            	    System.out.println(c.toString() +" selected");
	            	    selectedCards.add(c);
	            	} else {
	            	    System.out.println(c.toString() +" unselected");
	            	    selectedCards.remove(c);
	            	}
	            }
	        });
		}
        
     // Add submit
     		controls.add(submitButton);
     		submitButton.setPreferredSize(new Dimension(50, 80));
     		controls.add(clearButton);
     		clearButton.setPreferredSize(new Dimension(50, 80));
        
     	// Process submit button
    		submitButton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				System.out.println("Selected Cards: ");
    				for (Card c : selectedCards) {
    					System.out.println(c);
    				}
    				if (selectedCards.size() == 3) {
    					if (Game.isSet(selectedCards))
    						System.out.println("SET FOUND");
    					else
    						System.out.println("INVALID SET");
    				} else {
    					System.out
    							.println("Invalid Set! Sets must contain exactly 3 cards.");
    				}
//    				out.println("login|Andrew|andrew");
//    				try {
//    					System.out.println("This was recieved in the client");
//    					System.out.println(in.readLine());
//    				} catch (IOException e1) {
//    					e1.printStackTrace();
//    				}

    			}
    		});
    		
    		// Process clear button
    				clearButton.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent e) {
    						selectedCards.removeAll(selectedCards);
    						for (JToggleButton bC : cardButtons.values())
    							bC.setSelected(false);
    					}
    				});
        pane.add(compsToExperiment, BorderLayout.NORTH);
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(controls, BorderLayout.SOUTH);
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        
        //Create and set up the window.
        GameGridTest frame = new GameGridTest("SET GAME :O ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
//        frame.pack();
        int frameWidth = 800;
        int frameHeight = 600;
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
    }
    
//    public static void initServerConnection()
//    {
//        try 
//        {
//	        socket = new Socket(HOST, PORT);
//
//	        out = new PrintWriter(socket.getOutputStream(), true);
//
//	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//        }
//        catch (UnknownHostException e)
//        {
//            System.err.println("Cannot resolve host "+ HOST);
//        }
//        catch (IOException e)
//        {
//            System.err.println("Couldn't get I/O for the connection to the host "+HOST);
//        }
//    }
//    
    public static void main(String[] args) {
    	Game game1 = new Game();
		game1.init();
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
//        initServerConnection();
////        LOGIN HERE
//        out.println("login|Andrew|andrew");
//        try
//        {
//	        while ((inputLine = in.readLine()) != null)
//	        {
//	            System.out.println(inputLine);
//	        }
//	        System.out.println("Done");
//        }
//        catch (IOException e) 
//        {
//            e.printStackTrace(); 
//        }
        }
    }