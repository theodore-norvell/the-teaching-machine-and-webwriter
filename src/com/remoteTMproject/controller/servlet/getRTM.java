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
    private String guidTest1= createRTM.guidHolder;
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
		//获取请求资源路径
		String uri=request.getRequestURI();
		String path=uri.substring(
				uri.lastIndexOf("/"),
				uri.lastIndexOf("."));
		
		
		 //getState
		if(path.equals("/state")){
				
			if(  mapForRTM.getInstance(guidTest1).evaluator.getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guidTest1).evaluator.goForward();
			
				out.println("TMStatusCode is-------->READY") ;
				//System.out.println(exp);
			}
		}
		
		//getExpression
		if(path.equals("/expression")){
			
			String exp = mapForRTM.getInstance(guidTest1).evaluator.getExpression() ;
			out.println(exp);
		}
		
		//getAnswer
		if(path.equals("/answer")){
			while( mapForRTM.getInstance(guidTest1).getStatusCode() == TMStatusCode.READY ) {
				mapForRTM.getInstance(guidTest1).evaluator.goForward();
			}
			String myOutPut = mapForRTM.getInstance(guidTest1).getOutputString() ;
	        out.println("Answer is----------->"+myOutPut);
			
		}
		
		
	}

}
