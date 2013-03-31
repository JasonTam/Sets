package setClient;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import setServer.JSONinterface;

public class CreateRoomButton extends JPanel {
    
    public CreateRoomButton()
    {
        JButton createButton = new JButton("Create Room!");
        final JTextField roomName = new JTextField();
        
        setLayout(new GridLayout(2, 1));
        
        createButton.addActionListener
        (
            new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                if (roomName.getText() != "")
	                {
	                    
		                InitGame.out.println(JSONinterface.genericToJson("join", roomName.getText()));
		                roomName.setText("");
		                InitGame.changeCards("GAME");
	                }
	            } 
	        }
        );
        
        add(createButton);
        add(roomName);
        
        
        
    }

}
