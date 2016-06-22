package tm.gwt.telford;

import com.google.gwt.dom.client.Style;

public class LayoutPosition {
	int locType ; // location type 1:north 2:south 3:east 4:west 0:center
	int x, y;
	Style.Unit xStyle, yStyle;
	public LayoutPosition(int locType, int x, Style.Unit xStyle, int y, Style.Unit yStyle){
		this.locType = locType;
		this.x = x;
		this.y = y;
		this.xStyle = xStyle;
		this.yStyle = yStyle;
	}

}
