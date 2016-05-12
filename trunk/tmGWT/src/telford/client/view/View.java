package telford.client.view;

import com.google.gwt.user.client.ui.RootPanel;

import telford.client.tm.ExpressionDisplay;
import telford.common.Component;
import telford.common.Graphics;
import telford.common.Root;
public class View extends Component{
	Root root;
	public View(Root r) {
		root = r;
		repaint() ;
	}
	
	
	@Override public void paintComponent( Graphics g ) {
		super.paintComponent(g); // redraw canvas
		//add new canvas to root panel
		RootPanel rootPanel = (RootPanel)root.getPeer().getRepresentative();
		rootPanel.clear();
//		rootPanel.add(((MyComponent)this.getPeer().getRepresentative()).getCanvas());
		rootPanel.add(((ExpressionDisplay)this.getPeer().getRepresentative()).getCanvas());
	}
	
	public void terminate() {
		timer.stop() ;
	}
	
	public void start(){
		timer.start();
	}
	
	// It is important to use a Swing timer here
	private telford.common.Timer timer;
}

