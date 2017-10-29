package tm.gwt.display;

import tm.portableDisplays.PortableDisplayer;

public class Viewer {
	PortableDisplayer displayer;
	public Viewer(PortableDisplayer displayer) {
		super();
		this.displayer = displayer;
		repaint();
	}

	public void repaint() {
		displayer.refresh();
	}
	
	public PortableDisplayer getPlayer(){
		return this.displayer;
	}
}
