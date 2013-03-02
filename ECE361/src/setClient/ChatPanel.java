package setClient;

import java.awt.Dimension;
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

public class ChatPanel extends JPanel {
    
    private static JTextArea recievedText;
    private JTextField inputText;
    public static UserList userList;
    
    private String text;
    
    public class UserList extends JList
    {
        public UserList()
        {
            createListModel();
        }
        
        private void createListModel()
        {
            DefaultListModel listModel = new DefaultListModel(); 
            for (String eachUser : User.userList.keySet())
            {
                listModel.addElement(User.userList.get(eachUser));
            }
            setModel(listModel);
            
        }
        
    }
    
    public ChatPanel() 
    {
        recievedText = new JTextArea();
        inputText = new JTextField();
        userList = new UserList();
        
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
                    InitGame.out.println("chat " + inputText.getText());
	                text = inputText.getText();
	                inputText.setText("");
                }
            }
            
        });
        
//      Make the text area uneditable
        recievedText.setEditable(false);
        recievedText.setWrapStyleWord(true);
        
        
        setLayout(new GridLayout(3,1));
        
	    JPanel chatScroll = new JPanel();
        JScrollPane scrollFrame = new JScrollPane(chatScroll);
    
        add(userList);
        add(scrollFrame);            
        add(inputText);
        
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
