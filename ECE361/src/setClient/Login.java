package setClient;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import setServer.JSONinterface;
import setServer.SetServer;


import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;



public class Login extends JFrame  {
	
	JTextField jtfInput;

	JButton jbnLogin;
	JButton jbnRegister;
	JButton jbnTest;
	JLabel jlbOutMessage;
	JLabel jlbPassword;
	JLabel jlbUser;
	JLabel jlbTitle;
	JPasswordField jpwPassword;

	int currentLine;
	
	String newline = "\n";

	
	public Login() {

			jtfInput = new JTextField(20);
			
			

			jbnLogin = new JButton("Log In");
			jbnRegister = new JButton("Register");
			jlbOutMessage = new JLabel(" ");
			jlbPassword = new JLabel("User: ");
			jlbUser = new JLabel("Password: ");
			jpwPassword = new JPasswordField(20);
			jpwPassword.setEchoChar('*');

			jbnTest = new JButton("Play as guest");
			
			jlbTitle = new JLabel();
			jlbTitle.setText("Welcome to the Set Game");
			jlbTitle.setFont(new Font("Sans", Font.HANGING_BASELINE, 20));
			
			
	        addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	                InitGame.out.println(JSONinterface.genericToJson("quit", "quitting game"));
	                System.exit(0);
	            }
	        });

			jbnLogin.addActionListener(new ActionListener() {          
			    public void actionPerformed(ActionEvent e) {
			    	
			    	String userString = jtfInput.getText().toLowerCase();
			    	String passString = new String(jpwPassword.getPassword());
			    	
			    	ArrayList<String> data =  new ArrayList<String>();
			    	data.add(userString);
			    	data.add(passString);

					String loginString  = JSONinterface.genericToJson("login", data);
					
					InitGame.out.println(loginString);
					// Go to initGame loop for login-response
					
			    }
			});
			jbnTest.addActionListener(new ActionListener() {          
			    public void actionPerformed(ActionEvent e) {
			    	String username = "Andrew";
			    	String password = "andrew";
			    	
			    	jtfInput.setText(username);
			    	
			    	ArrayList<String> data =  new ArrayList<String>();
			    	data.add(username);
			    	data.add(password);

					String loginString  = JSONinterface.genericToJson("login", data);
					
					InitGame.out.println(loginString);
			    	InitGame.out.println(JSONinterface.genericToJson("rooms", "send me rooms"));
			    	InitGame.out.println(JSONinterface.genericToJson("users", "send me users"));
			    	
					setVisible(false);
					InitGame.showGame();
			    	
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
			contentPane.setPreferredSize(new Dimension(500, 200));
//			contentPane.setSize(new Dimension(400, 300));
			GridBagConstraints gridCons1 = new GridBagConstraints();
			gridCons1.gridx=2;
			gridCons1.gridy=2;
			contentPane.add(jtfInput, gridCons1);
			

			gridCons1.gridx=2;
			gridCons1.gridy=3;
			contentPane.add(jpwPassword, gridCons1);

			gridCons1.gridx=4;
			gridCons1.gridy=3;
			contentPane.add(jbnLogin, gridCons1);
			
			gridCons1.gridx=4;
			gridCons1.gridy=2;
			contentPane.add(jbnRegister, gridCons1);


			gridCons1.gridx=2;
			gridCons1.gridy=4;
			contentPane.add(jlbOutMessage, gridCons1);
			

			gridCons1.gridx=1;
			gridCons1.gridy=2;
			contentPane.add(jlbPassword, gridCons1);
			
			gridCons1.gridx=1;
			gridCons1.gridy=3;
			contentPane.add(jlbUser, gridCons1);
			
			gridCons1.gridx=2;
			gridCons1.gridy=1;
			contentPane.add(jlbTitle, gridCons1);
			
			gridCons1.gridx=4;
			gridCons1.gridy=4;
			contentPane.add(jbnTest, gridCons1);
			
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
	
	/*
	public static void main(String[] args) {
		InitGame.initServerConnection();
		Login jtfTfDemo = new Login();
		jtfTfDemo.pack();
		jtfTfDemo.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jtfTfDemo.setVisible(true);
	}
	*/

}
