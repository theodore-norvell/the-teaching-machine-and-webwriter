package tm.gwt.telford;
import com.google.gwt.canvas.dom.client.Context2d;

import telford.common.Font;

public class FontMetricsGWT extends telford.common.FontMetrics {
	Context2d context;
	Font font;

	FontMetricsGWT(Context2d c, Font f) {
		this.context = c;
		this.font = f;
	}

	@Override
	public int getHeight() {
		// TODO
		return (int) context.measureText("M").getWidth();
	}

	@Override
	public int stringWidth(String str) {
		return (int) context.measureText(str).getWidth();
	}

    @Override
    public int stringWidth(char[] chars, int i, int len) {
        String str = new String( chars, i, len ) ;
        return stringWidth( str ) ;
    }

	@Override
	public int getAscent() {
		return (int) context.measureText("M").getWidth();
	}
	
	@Override
	public int getDescent() {
		return 4;
//		return (int) context.measureText("M").getWidth()*2;
	}
}
