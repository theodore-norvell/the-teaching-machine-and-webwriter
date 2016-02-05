package com.remoteTMproject.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import tm.utilities.Debug;

import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.json.JsonTransfer;
import com.remoteTMproject.model.json.Response;
import com.remoteTMproject.model.map.mapForRTM;

/**
 * Servlet implementation class initializeTheState
 */
@WebServlet("/initializeTheState")
public class initializeTheState extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private int reasonFlag=0;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public initializeTheState() {
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
		if(path.equals("/initializeTheState")){
			String guid = request.getParameter("guid");
			String responseWantedFlag = request.getParameter("responseWantedFlag");
			String res=responseWantedFlag;

		
			remoteTM rtm = mapForRTM.getInstance(guid);
			
			JSONObject response1;
			try {
				response1 = rtm.initializeTheState(guid,responseWantedFlag);
				
				if("1".equals(res)){
					
					out.println(response1);
					out.close();
				}
				if("0".equals(res))
				{
				   out.close();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	
		
		
			
		}
		
		
	}

}
