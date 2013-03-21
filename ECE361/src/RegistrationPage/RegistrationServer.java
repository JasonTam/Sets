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


public class RegistrationServer extends AbstractHandler
{
	static String signup_template;
	
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(signup_template);
    }
 
    public static void main(String[] args) throws Exception
    {
    	
    	// read templates into memory
    	RegistrationServer.signup_template = readFile("src/RegistrationPage/templates/signup.html");
    	
    	
        Server server = new Server(8080);
        server.setHandler(new RegistrationServer());
 
        server.start();
        server.join();
    }
    
    /* recipe from stackoverflow */
    private static String readFile(String path) throws IOException {
    	  FileInputStream stream = new FileInputStream(new File(path));
    	  try {
    	    FileChannel fc = stream.getChannel();
    	    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
    	    /* Instead of using default, pass in a decoder. */
    	    return Charset.defaultCharset().decode(bb).toString();
    	  }
    	  finally {
    	    stream.close();
    	  }
    	}
}