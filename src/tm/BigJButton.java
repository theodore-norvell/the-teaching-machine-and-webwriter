package tm;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JButton;

/** A JButton that is sized to fit its text.
 * Idea and code from 
 * http://stackoverflow.com/questions/3485088/resizing-jbuttons-and-other-components-according-to-text
 **/
@SuppressWarnings("serial")
public class BigJButton extends JButton {

	public BigJButton() {
		super() ; }
	
	@Override
	public void setText(String arg0) {
	    super.setText(arg0);
	    FontMetrics metrics = getFontMetrics(getFont()); 
	    int width = metrics.stringWidth( getText() );
	    int height = metrics.getHeight();
	    Dimension newDimension =  new Dimension(width+40,height+10);
	    setPreferredSize(newDimension);
	    setBounds(new Rectangle(
	                   getLocation(), getPreferredSize()));
	}
	
}
