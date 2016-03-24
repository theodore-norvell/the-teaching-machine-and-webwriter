package telford.jse;

import java.awt.FlowLayout;

public class FlowLayoutJSE implements telford.common.FlowLayout {

	FlowLayout f = new FlowLayout();

	@Override
	public Object getRepresentative() {
		return f;
	}

}
