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

}
