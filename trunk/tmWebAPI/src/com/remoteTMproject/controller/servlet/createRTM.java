package com.remoteTMproject.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tm.interfaces.TMStatusCode;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.StringFileSource;
import tm.utilities.TMFile;

import com.remoteTMproject.controller.GUIDMaker.GUIDMaker;
import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.map.mapForRTM;

/**
 * Servlet implementation class servletTest
 * in the servletTest class i improve some stuff based on the servlet class
 */
@WebServlet("/servletTest")
public class createRTM extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//static public String guidHolder ;
       
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

		//test for outputs
		/*System.out.print("RTM is instantiated at this time:-------");
		System.out.println(RTMname);
		System.out.print("guid for the RTM is:-------------------");
		System.out.println(guidNumberTest);*/

		
		//create a remoteRTM for the first time if the url is    /createRemoteTM.do
	    if(path.equals("/createRemoteTM")){
	    	String programText = request.getParameter("Codes");
			
			StringFileSource fs = new StringFileSource() ;
			String fileName="test";
			fs.addString( fileName, programText ) ;
			TMFile tmf = new TMFile( fs, fileName ) ;

			Debug debug = Debug.getInstance() ;
			debug.deactivate(); 

			//getInstance could lazily create a remoteTM object and add the object to a ConcurrentHashmap
			mapForRTM.addInstance(guidNumberTest);
			remoteTM rtm = mapForRTM.getInstance(guidNumberTest);
			//loadTMFile, then compile, STATE ---------------------> COMPILED
			rtm.loadTMFile(rtm.CPP_LANG,tmf);
			
			Assert.check(rtm.getEvaluator().getStatusCode() == TMStatusCode.COMPILED );
			rtm.getEvaluator().initialize();
			Assert.check( rtm.getEvaluator().getStatusCode() == TMStatusCode.READY );
			if(rtm.getEvaluator().getStatusCode() == TMStatusCode.READY){
			out.println(guidNumberTest);
			out.println("   remote TMStatusCode is READY");
			}
	    }
	    
	    
	    
	    
	    
	    
   /* //getState
		if(path.equals("/state")){
				
			if(  mapForRTM.getInstance(guidNumberTest).evaluator.getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guidNumberTest).evaluator.goForward();
			
				out.println("READY") ;
				//System.out.println(exp);
			}
		}
		
		//getExpression
		if(path.equals("/expression")){
			
			String exp = mapForRTM.getInstance(guidNumberTest).evaluator.getExpression() ;
			out.println(exp);
		}
		
		//getAnswer
		if(path.equals("/answer")){
			while( mapForRTM.getInstance(guidNumberTest).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guidNumberTest).evaluator.goForward();
			}
			String myOutPut = mapForRTM.getInstance(guidNumberTest).getOutputString() ;
	        out.println(myOutPut);
			
		}*/
		
	    
	    
		
	
	}

}
