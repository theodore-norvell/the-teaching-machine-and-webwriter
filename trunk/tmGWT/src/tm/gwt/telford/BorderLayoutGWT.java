package tm.gwt.telford;

import com.google.gwt.dom.client.Style.Unit;

class BorderLayoutGWT implements telford.common.BorderLayout {

	LayoutPosition lp;

	@Override
	public Object getRepresentative() {
		return lp;
	}

	@Override
	public Object getNorth() {
		return new LayoutPosition(1, 5, Unit.EM, 5, Unit.EM);
	}

	@Override
	public Object getSouth() {
		return new LayoutPosition(2, 5, Unit.EM, 5, Unit.EM);
	}

	@Override
	public Object getEast() {
		return new LayoutPosition(3, 0, Unit.PCT, 50, Unit.PCT);
	}

	@Override
	public Object getWest() {
		return new LayoutPosition(4, 0, Unit.PCT, 50, Unit.PCT);
	}

	@Override
	public Object getCenter() {
		return new LayoutPosition(0, 5, Unit.EM, 5, Unit.EM);
	}

}
