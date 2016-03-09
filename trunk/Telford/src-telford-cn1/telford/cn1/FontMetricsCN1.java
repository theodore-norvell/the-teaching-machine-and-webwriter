package telford.cn1;

import com.codename1.ui.*;

class FontMetricsCN1 extends telford.common.FontMetrics {
	Font f ;
	
	FontMetricsCN1( Font f ) {this.f = f ; }

	@Override
	public int getHeight() {
		return f.getHeight() ;
	}

	@Override
	public int stringWidth(String str) {
		return f.stringWidth(str);
	}

	@Override
	public int getAscent() {
		return 0;
	}

}
