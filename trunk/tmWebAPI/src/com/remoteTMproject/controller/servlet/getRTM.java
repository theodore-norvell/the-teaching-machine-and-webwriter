/*package com.remoteTMproject.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import tm.interfaces.CodeLine;
import tm.interfaces.SourceCoords;
import tm.interfaces.TMStatusCode;
import tm.utilities.TMFile;

import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.json.JsonTransfer;
import com.remoteTMproject.model.json.Response;
import com.remoteTMproject.model.map.mapForRTM;

*//**
 * Servlet implementation class servletTest1
 *//*
@WebServlet("/servletTest1")
public class getRTM extends HttpServlet {
	private static final long serialVersionUID = 1L;
    //private String guidTest1= createRTM.guidHolder;

	private int focusnumber;
    *//**
     * @see HttpServlet#HttpServlet()
     *//*
    public getRTM() {
        super();
        // TODO Auto-generated constructor stub
    }

	*//**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 *//*
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	*//**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 *//*
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		
	    PrintWriter out = response.getWriter();
		//get the request uri
		String uri=request.getRequestURI();
		String path=uri.substring(
				uri.lastIndexOf("/"),
				uri.lastIndexOf("."));
		
		*//**JSONObject here **//*
		//JSONObject obj1 = new JSONObject();

		
		//intoSub
		if(path.equals("/intoSub")){
			String guid;
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).intoSub();
				//out.print("READY") ;
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_COMPLETE)
			{
				out.println("EXECUTION_COMPLETE");
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_FAILED)
			{
				out.println("EXECUTION_FAILED");
			}
			
		}
		//intoExp
		if(path.equals("/intoExp")){
			String guid;
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).intoExp();
				//out.print("READY") ;
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_COMPLETE)
			{
				out.println("EXECUTION_COMPLETE");
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_FAILED)
			{
				out.println("EXECUTION_FAILED");
			}
			
		}
		//overAll
		if(path.equals("/overAll")){
			String guid;
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).overAll();
				//out.print("READY") ;
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_COMPLETE)
			{
				out.println("EXECUTION_COMPLETE");
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_FAILED)
			{
				out.println("EXECUTION_FAILED");
			}
			
		}
		//microStep
		if(path.equals("/microStep")){
			String guid;
			guid = request.getParameter("myguid");
			if(  mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).microStep();
				//out.print("READY") ;
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_COMPLETE)
			{
				out.println("EXECUTION_COMPLETE");
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_FAILED)
			{
				out.println("EXECUTION_FAILED");
			}
			
		}
		
		//getExpression
		if(path.equals("/expression")){
			String guid;
			guid = request.getParameter("myguid");
			String exp = mapForRTM.getInstance(guid).getExpression() ;
			out.print(exp);
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_COMPLETE)
			{
				out.println("EXECUTION_COMPLETE");
			}
			if(mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.EXECUTION_FAILED)
			{
				out.println("EXECUTION_FAILED");
			}
			
			
		}
		
		//getAnswer
		if(path.equals("/answer")){
			String guid;
			guid = request.getParameter("myguid");
			while( mapForRTM.getInstance(guid).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guid).getEvaluator().goForward();
			}
			String myOutPut = mapForRTM.getInstance(guid).getOutputString() ;
	        out.print(myOutPut);
		}
		
		
		
		
		
	}

}
*/