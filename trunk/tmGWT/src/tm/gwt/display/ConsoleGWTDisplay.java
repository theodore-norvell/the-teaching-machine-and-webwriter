package tm.gwt.display;

import tm.gwt.jsInterface.GWTSuperTMFile;
import tm.interfaces.StateInterface;
import tm.portableDisplays.CodeDisplayer;
import tm.portableDisplays.ConsoleDisplayer;
import tm.portableDisplays.ExpressionDisplayer;
import tm.portableDisplays.PortableContextInterface;
import tm.portableDisplays.PortableDisplayer;

public class ConsoleGWTDisplay extends DisplayAdapter {

	private GWTSuperTMFile theFile;
	StateInterface evaluator;
	PortableContextInterface context = new GWTContext();
	
	
	
	public ConsoleGWTDisplay(StateInterface e, PortableContextInterface context) {
		super(new ConsoleDisplayer(e,context), "ConsoleDisplayPanel", "Console", 300, 100);
		this.evaluator = e;
		this.context = context;	
	}
	
	public void refresh(){
		//((ConsoleDisplayer) displayer).repaint();
		super.refresh();
	}
	

}
