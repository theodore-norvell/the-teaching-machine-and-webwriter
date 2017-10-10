package tm.gwt.telford;

import telford.common.MouseEvent;
import telford.common.MouseListener;
import tm.gwt.display.DisplayAdapterGWT;
import tm.gwt.display.GWTContext;
import tm.portableDisplays.PortableContextInterface;

public class MouseListenerGWT implements MouseListener {
	
	PortableContextInterface context = new GWTContext();

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseClick(MouseEvent e) {
		DisplayAdapterGWT eventSource = (DisplayAdapterGWT) e.getSource();
		eventSource.MouseJustClicked(e);
	}

}
