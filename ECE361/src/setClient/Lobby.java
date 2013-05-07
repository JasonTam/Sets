package setClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import setServer.GameRoom;
import setServer.JSONinterface;
import setServer.User;

public class Lobby extends JPanel
{
	 private RoomsPanel lobbyRooms;
     private JButton test;
     
     public CreateRoomPanel createRoom;
     
     
     public ChatPanel chat;
	 
	 public static ConcurrentHashMap<String, GameRoom> roomHash = new ConcurrentHashMap<String, GameRoom>();
	 
     public static ArrayList<GameRoom> roomArray = new ArrayList<GameRoom>();
     
     public static ArrayList<User> userArray = new ArrayList<User>();
     
     public static String userName = null;
	 
    public Lobby()
    {
    	InitGame.setTheme();
        test = new JButton("Click me to view an output of data structurs in teh server's error console.");
        chat = new ChatPanel();
        lobbyRooms = new RoomsPanel();
        createRoom = new CreateRoomPanel();
        
        lobbyRooms.setBorder(BorderFactory.createLineBorder(Color.black)); 
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        
        c.gridx = 0;
        c.gridy = 0;
        add(lobbyRooms, c);
        
//        c.gridx = 1;
//        c.gridy = 0;
//        add(chat, c);
        
//        c.gridx = 0;
//        c.gridy = 1;
//        c.gridwidth = 1;
//        add(test, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(createRoom, c);
        
        test.addActionListener
        (
            new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                System.out.println(InitGame.topIsLobby());
	                InitGame.out.println(JSONinterface.genericToJson("test", "setting up test button. REMOVE THIS CONDITION"));
//	                InitGame.changeCards("GAME");
	            } 
	        }
        );
        
    }
    
    public void updateLobbyPanel()
    {
        lobbyRooms.updateRoomListPanel();
        lobbyRooms.revalidate();
        lobbyRooms.repaint();
    }
    
    
    private class RoomsPanel extends JPanel
    {
	    private int numRooms = 1;
	    private int height;
	    private GridLayout gLayout;
	    private JButton[] roomButtons;
	    
	    private static final int width = 5;
	    
        private RoomsPanel()
        {
            
            /*
            JPanel test = new JPanel();
            JScrollPane scrollFrame = new JScrollPane(test);

            add(scrollFrame);            
            */
            
            height = (int) Math.ceil(numRooms / (width+ 0.0));
            gLayout = new GridLayout(height, width);
            
            setLayout(gLayout);
            
            gLayout.setHgap(10);
            gLayout.setVgap(10);
            
            addRooms();

        }
        
        private void removeRooms()
        {
            for (Component C : getComponents())
            {
                
                if ((new JButton()).getClass() == C.getClass())
                {
                    remove(C);
                }
                
            }
            
        }
        
        private void addRooms()
        {
            removeRooms();
            
            if (roomArray.isEmpty())
            {
                return;
            }
                    
            int i = 0;
            
            for (GameRoom room : roomArray) 
            {
                roomButtons[i] = new JButton(room.getName());
                roomButtons[i].setName(room.getName());
                add(roomButtons[i]);
                
                if (room.gameStarted)
                {
                    roomButtons[i].setEnabled(false);
                }
                if (room.usersInRoom.size() >= GameRoom.roomCapacity)
                {
                    roomButtons[i].setEnabled(false);
                }
                
	            roomButtons[i].addActionListener
		        (
		            new ActionListener()
			        {
			            public void actionPerformed(ActionEvent e)
			            {
			                String roomName = ((JButton)(e.getSource())).getText();
			                InitGame.gamePanel = new GamePanel(roomName);
			                InitGame.out.println(JSONinterface.genericToJson("join", roomName));
		                    InitGame.cardLayout.add(InitGame.gamePanel, "GAME");
			                InitGame.changeCards("GAME");
			                
			            } 
			        }
		        );
	            i++;
            }
            
            
            
        }
        
        private void updateRoomListPanel()
        {
            numRooms  = roomArray.size();
            height = (int) Math.ceil(numRooms / (width+ 0.0));
            
            
//            gLayout.setColumns(width);
//            gLayout.setRows(height);
            roomButtons = new JButton[numRooms];
            
            addRooms();
            
        }
        
    }

}
