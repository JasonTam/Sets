package setClient;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import setServer.JSONinterface;

public class CreateRoomPanel extends JPanel {
    
    private JButton createButton = new JButton("Create Room!");
    private JTextField roomName = new JTextField();
    
    public CreateRoomPanel()
    {
        
        setLayout(new GridLayout(1, 2));
        
        createButton.addActionListener
        (
            new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                if (!roomName.getText().equals(""))
	                {
		                InitGame.out.println(JSONinterface.genericToJson("create", roomName.getText()));
	                }
	                else
	                {	            		
	                    JOptionPane.showMessageDialog(InitGame.lobbyPanel, "Please enter a room name in the field to the left.  Then press this button.");
	                    
	                }
	            } 
	        }
        );
        
        add(roomName);
        add(createButton);
        
        
        
    }
   
    public void createRoomAction()
    {
        InitGame.gamePanel = new GamePanel(roomName.getText());
        InitGame.cardLayout.add(InitGame.gamePanel, "GAME");
        InitGame.out.println(JSONinterface.genericToJson("join", roomName.getText()));
        roomName.setText("");
        InitGame.changeCards("GAME");
    }

}
