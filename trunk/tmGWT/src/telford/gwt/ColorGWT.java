package telford.gwt;

import com.google.gwt.canvas.dom.client.CssColor;

public class ColorGWT implements telford.common.Color {

	CssColor c;
	int red, green, blue;

	public ColorGWT(int r, int g, int b) {
		if ((red > 255) || (red < 0) || (green > 255) || (green < 0) || (blue > 255) || (blue < 0)) {
			System.out.println(GWTUtil.error(GWTUtil.COLOR_INVALID_ARGUMENT, "ColorGWT()"));
		}
		red = r;
		green = g;
		blue = b;
		this.c = CssColor.make(r, g, b);
	}

	@Override
	public int getBlack() {
		return green;
	}

	@Override
	public int getRed() {
		return red;
	}

	@Override
	public int getBlue() {
		return blue;
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 * 
	 * @return a string representation of the receiver
	 */
	public String toString() {
		return "Color {" + red + ", " + green + ", " + blue + "}";
	}

	public CssColor toCssColor() {
		return c;
	}
}
