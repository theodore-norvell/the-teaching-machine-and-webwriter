package tm.portableDisplays;

import tm.interfaces.StateInterface ;

public abstract class PortableDisplayer extends telford.common.Canvas {

	protected StateInterface model;
	protected PortableContextInterface context;
	protected int vScale = 0;

	public PortableDisplayer(StateInterface model, PortableContextInterface context) {
		super();
		this.model = model;
		this.context = context;
	}

	public abstract void refresh();
	
	/**
	 * calculate position of focus line that is used set scrollTop value to its parent panel to avoid it off screen
	 * */
	public abstract int calFocusPosition();
	
	public int getVScale(){return vScale;}
}
