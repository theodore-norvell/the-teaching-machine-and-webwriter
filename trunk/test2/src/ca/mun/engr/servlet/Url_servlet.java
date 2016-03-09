
package ca.mun.engr;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JButton;

/**
 * Servlet implementation class Url_servlet
 */
@WebServlet("/Url_servlet")
public class Url_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Url_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.setIntHeader("refresh",1);
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		String source_image="http://localhost:8080/test/IMAGE_servlet";
//		String div_Id="output";
//		String type="text/javascript";
//		String source_js="/WEB-INF/Javascript/refresher.jsp";
//		
//		out.append("<html>");
//		out.append("<head>");
//		out.append("<title>JPanel</title>");
//		out.append("<script type="+type+" src="+source_js+"></script>");
//		out.append("</head>");
//		out.append("<body>");
//		out.append("<h3>Welcome to the JPanel</h3>");
//		out.append("<div id="+div_Id+"><img src="+source_image+"></div>");
//		out.append("</body>");
//		out.append("</html>");
		 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/Javascript/Display2.html");
		 dispatcher.include(request, response);
		  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
