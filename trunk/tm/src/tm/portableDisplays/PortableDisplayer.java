package tm.portableDisplays;

import telford.common.MouseEvent;
import telford.common.MouseListener;
import tm.interfaces.StateInterface ;

public abstract class PortableDisplayer extends telford.common.Canvas {

	protected StateInterface model;
	protected PortableContextInterface context;

	public PortableDisplayer(StateInterface model, PortableContextInterface context) {
		super();
		this.model = model;
		this.context = context;
		
		addMouseListener( new MouseListener() {
			@Override
			public void mouseMoved(telford.common.MouseEvent e) {

			}
			@Override
			public void mouseClick(telford.common.MouseEvent e) {
				mouseJustClicked(e);
			} } );
	}

	public void refresh() {
	    repaint() ;
	}
	
	public abstract void mouseJustClicked(MouseEvent evt);
	
}
