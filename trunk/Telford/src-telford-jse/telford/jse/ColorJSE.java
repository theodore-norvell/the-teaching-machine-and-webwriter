package telford.jse;

import java.awt.Color;

public class ColorJSE implements telford.common.Color {
	
	Color c;
	
	ColorJSE(Color c){
		this.c = c;
	}

	public int getBlack() {
		return Color.BLACK.getRGB();
	}

	public int getRed() {
		return Color.RED.getRGB();
	}

	@Override
	public int getBlue() {
		return Color.BLUE.getRGB();
	}
}
