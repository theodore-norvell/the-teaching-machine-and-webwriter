package telford.jse;

import java.awt.BorderLayout;

class BorderLayoutJSE implements telford.common.BorderLayout{
	
	BorderLayout b = new BorderLayout();

	@Override
	public Object getNorth() {
		return BorderLayout.NORTH;
	}

	@Override
	public Object getRepresentative() {
		return b;
	}
	
}
