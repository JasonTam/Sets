package setClient;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel {
    
    private JTextArea recievedText = new JTextArea();
    private JTextField inputText = new JTextField();
    
    private String text;
    
    public ChatPanel() 
    {
        inputText.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (inputText.getText() != "")
                {
                    InitGame.out.println("chat " + inputText.getText());
	                text = inputText.getText();
	                inputText.setText("");
                }
            }
            
        });
        
        recievedText.setEditable(false);
        
        setLayout(new GridLayout(3,1));
        
        add(inputText);
        add(recievedText);
       
    }
    
    public void displayMessage(String chatMessage)
    {
        recievedText.append(chatMessage);
        recievedText.append("\n");
    }
    public void sendMessage(String chatMessage)
    {
        
    }
    
    

}
