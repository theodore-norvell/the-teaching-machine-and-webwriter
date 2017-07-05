package telford.jse;

import java.awt.*;

public class FontMetricsJSE extends telford.common.FontMetrics {
	FontMetrics fm ;
	
	FontMetricsJSE( FontMetrics fm ) {this.fm = fm ; }
	
	@Override
	public int getHeight() {
		return fm.getHeight() ;
	}

	@Override
	public int stringWidth(String str) {
		return fm.stringWidth( str ) ;
	}

    @Override
    public int stringWidth(char[] chars, int i, int len) {
        return fm.charsWidth( chars, i, len ) ;
    }

	@Override
	public int getAscent() {
		return fm.getAscent();
	}
	
	@Override
	public int getDescent() {
		return fm.getDescent();
	}

}
