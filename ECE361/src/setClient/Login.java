package setClient;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import setServer.SetServer;


import java.security.MessageDigest;
import java.sql.SQLException;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;



public class Login extends JFrame  {
//		public static PrintWriter out;
//	    public static BufferedReader in;
//	    
//	    public static int PORT = 4444;
//	    public static String HOST = "localhost";
//	  
//	    
//	    private static Socket socket = null;
//	    
//	    private static boolean isConnected = true;
	
	JTextField jtfInput;

	JButton jbnLogin;
	JButton jbnRegister;
	JLabel jlbOutMessage;
	JLabel jlbPassword;
	JLabel jlbUser;
	JPasswordField jpwPassword;
	//JTextArea jtAreaOutput;
	String newline = "\n";

	
	public Login() {

			jtfInput = new JTextField(20);
			//jtfInput.addActionListener(this);
			
			

			jbnLogin = new JButton("Log In");
			jbnRegister = new JButton("Register");
			jlbOutMessage = new JLabel(" ");
			jlbPassword = new JLabel("User: ");
			jlbUser = new JLabel("Password: ");
			jpwPassword = new JPasswordField(20);
			jpwPassword.setEchoChar('*');

			//jtAreaOutput = new JTextArea(5, 20);
			//jtAreaOutput.setEditable(false);
			//JScrollPane scrollPane = new JScrollPane(jtAreaOutput,
					//JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					//JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

			jbnLogin.setActionCommand("login");

			jbnLogin.addActionListener(new ActionListener() {          
			    public void actionPerformed(ActionEvent e) {

			    	//checkPass();
			    	InitGame.initServerConnection();
			    	String passString = new String(jpwPassword.getPassword());
					String userString = jtfInput.getText();		
					StringBuffer loginString  = new StringBuffer();
					loginString.append("login|");
					loginString.append(userString);
					loginString.append("|");
					loginString.append(passString);
					
	
						InitGame.out.println(loginString);
						InitGame.out.println("rooms");
				    	InitGame.out.println("users");
				    	//InitGame.in.readLine();

			    		if(SetServer.allThreads.isEmpty())
			    		{
			    		// = InitGame.socket.getInputStream();
						try {
							jlbOutMessage.setText(InitGame.in.readLine());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    		}else
			    		{
			    			jlbOutMessage.setText("empty");
			    		}
//					
//				    	if(	SetMultiThread.out.toString().equalsIgnoreCase("Bad login information") )
//				    	{
//				    		//jlbOutMessage.setText("correct!");
//				    	}
				   

			    	
			    }
			});
			
			
			jbnRegister.addActionListener(new ActionListener() {          
			    public void actionPerformed(ActionEvent e) {
			    	openRegisterPage();
			
			    	
			    }
			});
			
			GridBagLayout gridBag = new GridBagLayout();
			Container contentPane = getContentPane();
			contentPane.setLayout(gridBag);
			
			GridBagConstraints gridCons1 = new GridBagConstraints();
			gridCons1.gridx=2;
			gridCons1.gridy=1;
			contentPane.add(jtfInput, gridCons1);
			

			gridCons1.gridx=2;
			gridCons1.gridy=2;
			contentPane.add(jpwPassword, gridCons1);

			gridCons1.gridx=4;
			gridCons1.gridy=2;
			contentPane.add(jbnLogin, gridCons1);
			
			gridCons1.gridx=4;
			gridCons1.gridy=1;
			contentPane.add(jbnRegister, gridCons1);


			gridCons1.gridx=2;
			gridCons1.gridy=3;
			contentPane.add(jlbOutMessage, gridCons1);
			

			gridCons1.gridx=1;
			gridCons1.gridy=1;
			contentPane.add(jlbPassword, gridCons1);
			
			gridCons1.gridx=1;
			gridCons1.gridy=2;
			contentPane.add(jlbUser, gridCons1);
	}

	
	
	private void openRegisterPage(){
		
    	try {
			Desktop.getDesktop().browse(new URI("http://localhost:8080"));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		
	}
//	private void checkPass() {
//
//		
//			//char[] inputPassword = jpwPassword.getPassword();
//			String passString = new String(jpwPassword.getPassword());
//			String userString = jtfInput.getText();
//			try {
//				
//				if(db.validateUser(userString,passString))
//		    	{
//		    		jlbOutMessage.setText("correct!");
//		    	}
//		    	else
//		    	{
//		    		jlbOutMessage.setText("inccorect!");
//		    	}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			char[] actualPassword ={ 'p', 'a', 's', 's' };
//			if (inputPassword.length != actualPassword.length)
//			{
//				return false;
//			}
//				
//			for (int i = 0; i < inputPassword.length; i++)
//			{
//				if (inputPassword[i] != actualPassword[i])
//				{
//					return false;
//				}
//			}
//			return true;		
		//jtAreaOutput.append(text + newline);


//	}
	public static void main(String[] args) {
		new Login();
		
		Login jtfTfDemo = new Login();
		jtfTfDemo.pack();
		jtfTfDemo.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jtfTfDemo.setVisible(true);
	}

}
