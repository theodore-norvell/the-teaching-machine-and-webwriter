package telford.jse;

import java.awt.Color;

public class ColorJSE implements telford.common.Color {
	
	static Color c;
	
	ColorJSE(Color c){
		ColorJSE.c = c;
	}

	public int getGreen() {
		return c.getGreen();
	}

	public int getRed() {
		return c.getRed() ;
	}

	@Override
	public int getBlue() {
		return c.getBlue() ;
	}
}
