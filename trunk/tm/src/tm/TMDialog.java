package tm;

import java.awt.Container;
import java.awt.Dimension ;
import java.awt.Rectangle ;

import javax.swing.BoxLayout ;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable ;

public class TMDialog extends JScrollPane {
    protected ScrollablePanel mainPanel ;
	protected TMMainPanel tmMainPanel ;
	
	TMDialog( TMMainPanel tmMainPanel ) {
	    this.tmMainPanel = tmMainPanel ;
	    mainPanel = new ScrollablePanel() ;
	    mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.Y_AXIS ) ) ;
	    this.setViewportView( mainPanel ) ;
	}
	
    public void display() {
        tmMainPanel.showDialog( this );
    }
	
	protected void dismiss() {
		tmMainPanel.removeDialog( this ) ;
	}
	
	@SuppressWarnings("serial")
	protected static class ScrollablePanel extends JPanel implements Scrollable {
	    public Dimension getPreferredScrollableViewportSize() {
	        return super.getPreferredSize(); //tell the JScrollPane that we want to be our 'preferredSize' - but later, we'll say that vertically, it should scroll.
	    }

	    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
	        return 16;
	    }

	    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
	        return 16;
	    }

	    public boolean getScrollableTracksViewportWidth() {
	        return true;//track the width, and re-size as needed.
	    }

	    public boolean getScrollableTracksViewportHeight() {
	        return false; //we don't want to track the height, because we want to scroll vertically.
	    }
	}
}