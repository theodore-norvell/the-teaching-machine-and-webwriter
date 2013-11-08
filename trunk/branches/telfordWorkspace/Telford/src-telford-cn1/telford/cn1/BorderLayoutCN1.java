package telford.cn1;

import com.codename1.ui.layouts.BorderLayout;


public class BorderLayoutCN1 implements telford.common.BorderLayout{
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
	public Object getCenter() {
		return BorderLayout.CENTER;
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
	
}
