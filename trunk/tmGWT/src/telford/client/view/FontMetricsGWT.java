package telford.client.view;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Instances of this class may be used to calculate the width of any font. This
 * class works best with fixed fonts, where each character shares a common
 * width/height.
 * 
 * This class uses a hidden div along with the various font details set upon
 * this instance. Unfortunately not all browsers make the
 * offsetWidth/offsetHeight immediately available requiring the user of a
 * deferred command, after setting properties and calling {@link #calculate()}.
 * 
 * In cases when {@link #isReady()} returns false after setting the other
 * properties use a deferred command.
 * 
 * @author Miroslav Pokorny
 */
public class FontMetricsGWT extends telford.common.FontMetrics {
	Context2d c ;
	
	FontMetricsGWT( Context2d c ) {this.c = c ; }
	
	@Override
	public int getHeight() {
		//TODO
		//the height of the text in pixels is equal to the font size in pts when the font is defined with the font property of the canvas context
		return (int)c.measureText("M").getWidth();
	}

	@Override
	public int stringWidth(String str) {
		return (int)c.measureText(str).getWidth();
	}

	@Override
	public int getAscent() {
//		return c.measureText("");
		return (int)c.measureText("M").getWidth();
	}
}
