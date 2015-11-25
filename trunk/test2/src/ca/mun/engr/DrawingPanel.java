package ca.mun.engr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

class DrawingPanel extends JPanel {
    /**
	 * 
	 */
	DrawingPanel(){
		setSize(300,300);
	}
	private static final long serialVersionUID = 1L;
	public List<Shape> shapes = new ArrayList<Shape>();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
    }

    public void addCircle(Circle circle) {
        shapes.add(circle);
        repaint();
    }
    public void addSquare(Square square){
    	shapes.add(square);
    	repaint();
    }
    public void addTriangle(Triangle triangle){
    	shapes.add(triangle);
    	repaint();
    }

    
//    public BufferedImage getScreenshot(){
//    	BufferedImage bi = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);  
//    	Graphics g = bi.createGraphics();
//        this.paint(g);  //this == JComponent
//        g.dispose();
//        return bi;
//    }

}