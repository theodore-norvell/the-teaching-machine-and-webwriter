package com.remoteTMproject.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tm.interfaces.CodeLine;
import tm.interfaces.SourceCoords;
import tm.interfaces.TMStatusCode;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;

import com.remoteTMproject.controller.GUIDMaker.GUIDMaker;
import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.json.JsonTransfer;
import com.remoteTMproject.model.json.Response;
import com.remoteTMproject.model.map.mapForRTM;

/**
 * Servlet implementation class servletTest
 * in the servletTest class i improve some stuff based on the servlet class
 */
@WebServlet("/servletTest")
public class createRTM extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//static public String guidHolder ;
	private int focusNumber;
    private int reasonFlag=0;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public createRTM() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
		//get the request url
		String uri=request.getRequestURI();
		String path=uri.substring(
				uri.lastIndexOf("/"),
				uri.lastIndexOf("."));
		
		/*instantiate a GUIDMaker object and call method to generate a guid*/
		GUIDMaker guid=GUIDMaker.getInstance();
		String myguid=guid.nextGUID();
		
		//System.out.println(myguid);
		String[] guidParts=myguid.split(":");
		//the RTM name
		String RTMname=guidParts[0];
		//THE guid number
		String guidNumberTest=guidParts[1];
		//guidHolder= guidNumberTest;
	  
		
		
		//create a remoteRTM for the first time if the url is    /createRemoteTM.do
		if(path.equals("/createRemoteTM")){
			
			Debug debug = Debug.getInstance() ;
			debug.deactivate(); 
					
			//getInstance could lazily create a remoteTM object and add the object to a ConcurrentHashmap
			mapForRTM.addInstance(guidNumberTest);
			remoteTM rtm = mapForRTM.getInstance(guidNumberTest);

			JSONObject response1;
			try {
				response1 = rtm.createRemoteTM(guidNumberTest);
				
				out.println(response1);
				out.close();
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			




				
	    }
	    
	}
	    
	    
	    
	    

	


}
