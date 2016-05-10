package tm;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class TMDialog extends JPanel {
	protected void dismiss() {
		Container parent = getParent() ;
		if( parent != null ) parent.remove( this ); 
	}
}
