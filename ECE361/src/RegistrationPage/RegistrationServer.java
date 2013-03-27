// example code from http://wiki.eclipse.org/Jetty/Tutorial/Jetty_HelloWorld

package RegistrationPage;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.FileInputStream;
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import de.neuland.jade4j.Jade4J;
import DBI.DBConnect;

// Wanted to use Jade template engine, the dependencies are absolutely nuts.

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
    	response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		Map<String, Object> view = new HashMap<String, Object>();
		view.put("notify", "");
		view.put("error", "");
		/* If we have form data, add response to view */
		if(request.getMethod().equalsIgnoreCase("POST")){
			try {
				String u = request.getParameter("username");
				String p = request.getParameter("password");
				//db.createUser(u, p);
				view.put("notify", "Successfully Created User "+u);
			} catch (Throwable e) {
				System.err.println("no parameters!");
				view.put("error", "Error Creating User");
			}
		}
		String html = Jade4J.render("src/RegistrationPage/templates/index.jade", view);
		response.getWriter().println(html);
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        RegistrationServer rs = new RegistrationServer();
        server.setHandler(new RegistrationServer());
        server.start();
        server.join();
    }
    
}