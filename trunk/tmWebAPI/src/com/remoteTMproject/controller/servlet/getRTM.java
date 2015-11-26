package com.remoteTMproject.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tm.interfaces.TMStatusCode;

import com.remoteTMproject.model.map.mapForRTM;

/**
 * Servlet implementation class servletTest1
 */
@WebServlet("/servletTest1")
public class getRTM extends HttpServlet {
	private static final long serialVersionUID = 1L;
    //private String guidTest1= createRTM.guidHolder;
	private String guid;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getRTM() {
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
		//get the request uri
		String uri=request.getRequestURI();
		String path=uri.substring(
				uri.lastIndexOf("/"),
				uri.lastIndexOf("."));
		
		
		 //goForward
		if(path.equals("/go")){
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getEvaluator().getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).getEvaluator().goForward();
				out.println("READY") ;
				//System.out.println(exp);
			}
		}
		//goBack
		if(path.equals("/goBack")){
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getEvaluator().getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).getEvaluator().goBack();
				out.println("READY") ;
			}
			
			
		}
		
		
		//redo
		if(path.equals("/redo")){
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getEvaluator().getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).getEvaluator().redo();
				out.println("READY") ;
			}
			
			
		}
		
		//getExpression
		if(path.equals("/expression")){
			guid = request.getParameter("myguid");
			String exp = mapForRTM.getInstance(guid).getEvaluator().getExpression() ;
			out.println(exp);
		}
		
		//getAnswer
		if(path.equals("/answer")){
			guid = request.getParameter("myguid");
			while( mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).getEvaluator().goForward();
			}
			String myOutPut = mapForRTM.getInstance(guid).getOutputString() ;
	        out.println(myOutPut);
			
		}
		
		
		
		
		
	}

}
