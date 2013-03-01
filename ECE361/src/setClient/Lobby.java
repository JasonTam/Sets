package setClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Lobby extends JPanel
{
	 private RoomsPanel lobbyRooms;
     private JButton test;
     private ChatPanel chat;
	 
	 private ConcurrentHashMap<String, Rooms> roomHash = new ConcurrentHashMap<String, Rooms>();
	 
    public Lobby()
    {
        test = new JButton();
        chat = new ChatPanel();
        lobbyRooms = new RoomsPanel();
        lobbyRooms.setBorder(BorderFactory.createLineBorder(Color.black)); 
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        
        c.gridx = 0;
        c.gridy = 0;
        add(lobbyRooms, c);
        
        c.gridx = 1;
        c.gridy = 0;
        add(chat, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(test, c);
        
        test.addActionListener
        (
            new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                InitGame.changeCards("CHAT");
	            } 
	        }
        );
        
    }
    
    public void updateLobbyPanel()
    {
        lobbyRooms.updateRoomListPanel();
        lobbyRooms.revalidate();
        
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
                
                    System.out.println("Showing Rooms: ");
                    System.out.println(C);
                if ((new JButton()).getClass() == C.getClass())
                {
                    remove(C);
                    System.out.println("hi!!!!");
                }
                
            }
            
        }
        
        private void addRooms()
        {
            removeRooms();
            
            if (Rooms.roomList.isEmpty())
            {
                return;
            }
                    
            int i = 0;
            
            for (Rooms room : Rooms.roomList) 
            {
                roomButtons[i] = new JButton(room.toString());
                roomButtons[i].setName(room.toString());
                add(roomButtons[i]);
                
	            roomButtons[i].addActionListener
		        (
		            new ActionListener()
			        {
			            public void actionPerformed(ActionEvent e)
			            {
			                System.out.println(((JButton)(e.getSource())).getText()+ " Clicked !");
			                
			                String roomString = "ROOMCREATE|who";
					        System.out.println("Initialsustring");
					        System.out.println(roomString);
					        
					        roomString = roomString.substring(roomString.indexOf("|"));
					        
					        System.out.println("Room sustring");
					        System.out.println(roomString);
			            } 
			        }
		        );
	            i++;
            }
            
            
            
        }
        
        private void updateRoomListPanel()
        {
            numRooms  = Rooms.roomList.size();
            height = (int) Math.ceil(numRooms / (width+ 0.0));
            
            
//            gLayout.setColumns(width);
//            gLayout.setRows(height);
            roomButtons = new JButton[numRooms];
            
            addRooms();
            
        }
        
    }

}