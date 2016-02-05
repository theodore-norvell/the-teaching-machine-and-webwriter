package com.remoteTMproject.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.remoteTMproject.model.RTM.remoteTM;
import com.remoteTMproject.model.map.mapForRTM;

/**
 * Servlet implementation class redo
 */
@WebServlet("/redo")
public class redo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public redo() {
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
		
		
		//redo
		if(path.equals("/redo")){
			String guid;
			guid = request.getParameter("myguid");
			
			remoteTM rtm= mapForRTM.getInstance(guid);
			JSONObject response1=rtm.redo(guid);
			
			out.println(response1);
		}
	}

}
