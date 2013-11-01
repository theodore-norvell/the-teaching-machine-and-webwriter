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

	@Override
	public Object getSouth() {
		return BorderLayout.SOUTH;
	}

	@Override
	public Object getEast() {
		return BorderLayout.EAST;
	}

	@Override
	public Object getWest() {
		return BorderLayout.WEST;
	}

	@Override
	public Object getCenter() {
		return BorderLayout.CENTER;
	}
	
}
