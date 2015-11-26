package com.remoteTMproject.controller.servlet;



import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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

import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.map.map;

/**
 * Servlet implementation class servlet
 * in the start of this project , create this servlet class. the first version!!!!!!!!!!!!!!!!!
 */
@WebServlet("/servlet")
public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servlet() {
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
		//获取请求资源路径
		String uri=request.getRequestURI();
		String path=uri.substring(
				uri.lastIndexOf("/"),
				uri.lastIndexOf("."));
		
		//instantiate a map if not exists, if exists return that one.
	
		//create a remoteRTM for the first time.
	    if(path.equals("/createRemoteTM")){
	    	//createRTM method to create a RTM
	        /** Status Code -- No Evaluator is present. This means no display manager either. */
	       // public static final int NO_EVALUATOR = 0 ;
	        
	        /**  Status Code -- An evaluator is built but compilation hasn't happened yet.  */
	       // public static final int READY_TO_COMPILE = 1 ;
	        
	        /**  Status Code -- Compilation failed. */
	       // public static final int DID_NOT_COMPILE = 2 ;
	        
	        /**  Status Code -- Compilation was successful. Initialization has not been done.*/
	        //public static final int COMPILED = 3 ;
	        
	        /**  Status Code -- Ready for next step*/
	       // public static final int READY = 4 ;
	    	
	    	String programText = request.getParameter("Codes");
			
			StringFileSource fs = new StringFileSource() ;
			String fileName="test";
			fs.addString( fileName, programText ) ;
			TMFile tmf = new TMFile( fs, fileName ) ;

			Debug debug = Debug.getInstance() ;
			debug.deactivate(); 
			//instantiate a remoteTM and add that to a map.
			remoteTM rtm=new remoteTM();
			map.addMap(rtm);
			rtm.loadTMFile(rtm.CPP_LANG,tmf);
			
			Assert.check(rtm.getEvaluator().getStatusCode() == TMStatusCode.COMPILED );
			rtm.getEvaluator().initialize();
			Assert.check( rtm.getEvaluator().getStatusCode() == TMStatusCode.READY );
			if(rtm.getEvaluator().getStatusCode() == TMStatusCode.READY){
			out.println("remote TMStatusCode is READY");
			}
	    }
	    
	    //getState
		if(path.equals("/state")){
				
			if(  map.getMap(1).getEvaluator().getStatusCode() == TMStatusCode.READY ) {
				map.getMap(1).getEvaluator().goForward();
			
				out.println("READY") ;
				//System.out.println(exp);
			}
		}
		
		//getExpression
		if(path.equals("/expression")){
			
			String exp = map.getMap(1).getEvaluator().getExpression() ;
			out.println(exp);
		}
		
		if(path.equals("/answer")){
			while(  map.getMap(1).getStatusCode() == TMStatusCode.READY ) {
				map.getMap(1).getEvaluator().goForward();
			}
			String myOutPut = map.getMap(1).getOutputString() ;
	        out.println(myOutPut);
			
		}
		
	    
	    
		
	}

}
