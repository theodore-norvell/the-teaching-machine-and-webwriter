package ca.mun.engr.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.mun.engr.ExternalRunner;
import sun.misc.BASE64Encoder;

/**
 * Servlet implementation class IMAGE_servlet
 */
@WebServlet("/IMAGE_servlet")
public class IMAGE_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Map<Integer, BufferedImage> comparator = new HashMap<Integer, BufferedImage>();
	private BufferedImage nullBI = null;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public IMAGE_servlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.setIntHeader("Refresh", 0);
//		Cookie[] cookies = request.getCookies();
//        int index = -1;
//        
//        for(int i = 0; i < cookies.length; i++) { 
//            Cookie cookie1 = cookies[i];
//            if (cookie1.getName().equals("index")) {
//            	index = Integer.parseInt(cookie1.getValue());
//            	}
//        }
		
		int idx = -1;
		try{
			idx = Integer.parseInt(request.getParameter("index"));
		}
		catch(java.lang.NumberFormatException e){
			// ignore this exception
			return;
		}
		
//		System.out.println(idx);
//		response.setContentType("image/png");
		
		ExternalRunner erImage = ExternalRunner.getInstance();
//		BufferedImage temp = erImage.ShotRequest(idx);
	
		BufferedImage biNew = erImage.ShotRequest(idx);
		
		
		
		if (comparator.get(idx)==null){
			comparator.put(idx, biNew);
			output(erImage.ShotRequest(idx),response);
		}else{
			
//			File outputfile1 = new File("/Users/leonardo/Documents/the-teaching-machine-and-webwriter/imageold.jpg");
//			ImageIO.write(comparator.get(idx), "png", outputfile1);
//			
//			File outputfile2 = new File("/Users/leonardo/Documents/the-teaching-machine-and-webwriter/imagenew.jpg");
//			ImageIO.write(biNew, "png", outputfile2);
			System.out.println(erImage.bufferedImagesEqual(comparator.get(idx),biNew));
			if(erImage.bufferedImagesEqual(comparator.get(idx),biNew)){
				output(nullBI, response);
			}else{
				output(biNew,response);
				comparator.put(idx, biNew);
			}

		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
	}
	
	private void output(BufferedImage bi,HttpServletResponse resp) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( bi, "png", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		 
		BASE64Encoder encoder = new BASE64Encoder();
		
		String base64 = encoder.encode(imageInByte);
//		BufferedInputStream bis = new BufferedInputStream(base64);
		
        ServletOutputStream out = resp.getOutputStream();
        out.print(base64);
//		ImageIO.write(erImage.ShotRequest(idx), "png", out);
		
		out.close();
	}
}
