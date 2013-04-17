// example code from http://wiki.eclipse.org/Jetty/Tutorial/Jetty_HelloWorld

package RegistrationPage;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.FileInputStream;
import java.io.IOException;
 
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.neuland.jade4j.Jade4J;
import DBI.DBConnect;
import DBI.User;

public class RegistrationServer extends AbstractHandler
{
	private DBConnect db;
	
	public RegistrationServer()
	{	
    	this.db = new DBConnect();	
	}
	
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException
    {
    	if(!request.getRequestURI().equals("/")){
    		return;
    	}
    	response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		Map<String, Object> view = new HashMap<String, Object>();
		view.put("notify", "");
		view.put("error", "");
		/* If we have form data, add response to view */
		if(request.getMethod().equalsIgnoreCase("POST")){
			Map<String, String[]> parameters = request.getParameterMap();
			try {
				String u = request.getParameter("username");
				String p = request.getParameter("password");
				if (parameters.containsKey("newpassword")){
					boolean worked = db.updateUserPassword(u, p, request.getParameter("newpassword"));
					if (worked){
						view.put("notify", "Changed Password for User "+u);
					} else {
						view.put("error", "Username and Password incorrect.");
					}
				} else {
					db.createUser(u, p);
					view.put("notify", "Successfully Created User "+u);
				}
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e){
				view.put("error", "User already exists!");
			}
			catch (Throwable e) {
				e.printStackTrace();
				System.err.println("no parameters!");
				view.put("error", "Error Creating User");
			}
		}
		try {
			List<User> I = db.getStats();
			view.put("scores", I);
		} catch (Exception e){
			e.printStackTrace();
		}
		baseRequest.setHandled(true);
		String html = Jade4J.render("src/RegistrationPage/templates/index.jade", view);
		response.getWriter().println(html);
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        RegistrationServer rs = new RegistrationServer();
        //server.setHandler(new RegistrationServer());

        ResourceHandler resource_handler = new ResourceHandler();
        //resource_handler.setDirectoriesListed(false);
        // resource_handler.setWelcomeFiles(new String[]{ "index.jade" });
        resource_handler.setServer(server);
        rs.setServer(server);
        resource_handler.setResourceBase("./src/RegistrationPage");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {rs , resource_handler});
        
        
        server.setHandler(handlers);
        
        server.start();
        server.join();
    }
    
}