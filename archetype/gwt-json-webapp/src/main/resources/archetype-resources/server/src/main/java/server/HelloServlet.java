package ${package}.server;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ${package}.json.ModelJSON;
import ${package}.model.Model.Greet;

/**
 * HelloServlet
 * 
 */

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet
{
    
    final AtomicInteger greetCount = new AtomicInteger(0);
    final ModelJSON json = new ModelJSON();
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        response.setContentType("application/json");
        
        Greet.Builder greet = Greet.newBuilder();
        json.mergeFrom(request.getInputStream(), greet);
        
        String name = greet.getName();
        if(name==null || name.length()==0)
            greet.setName("Anonymous");
       
        greet.setId(greetCount.incrementAndGet())
            .setStatus(Greet.Status.ACKNOWLEDGED)
            .setMessage("Hello " + greet.getName() + " from server @ " + new Date());
        
        json.writeTo(response.getOutputStream(), greet.build());
    }

}

