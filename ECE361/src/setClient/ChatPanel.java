package setClient;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

import setServer.JSONinterface;

public class ChatPanel extends JPanel {
    
    private static JTextArea recievedText;
    private JTextField inputText;
    
    private String text;
    
    
    public ChatPanel() 
    {
        recievedText = new JTextArea();
        inputText = new JTextField();
        
//        Cause the text area to always move down with new text
	    DefaultCaret caret = (DefaultCaret)recievedText.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    
//	    Make the text field submit text on 'enter'
        inputText.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (inputText.getText() != "")
                {
	                text = inputText.getText();
                    InitGame.out.println(JSONinterface.genericToJson("chat", text));
	                inputText.setText("");
                }
            }
            
        });
        
//      Make the text area uneditable
        recievedText.setEditable(false);
        recievedText.setWrapStyleWord(true);
        
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
	    JPanel chatScroll = new JPanel();
        JScrollPane scrollFrame = new JScrollPane(chatScroll);
    
        
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        
        
        c.gridy = 0;
        c.weighty = 1;
        add(scrollFrame, c);            
        
        c.gridy = 1;
        c.weighty = 0.2;
        add(inputText, c);
        
        chatScroll.setLayout(new GridLayout(1,1));
        chatScroll.setAutoscrolls(true);
        scrollFrame.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        chatScroll.add(recievedText);
       
    }
    
    public void displayMessage(String chatMessage)
    {
        recievedText.append(chatMessage);
        recievedText.append("\n");
    }
    
    
    
}
