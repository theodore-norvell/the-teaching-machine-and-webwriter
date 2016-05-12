package telford.client.view;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import telford.common.Color;
import telford.common.Font;
import telford.common.Line;

public class GraphicsGWT implements telford.common.Graphics {

	Context2d c;
	Color currColor;
	Font currFont;

	public GraphicsGWT(Context2d c) {
		this.c = c;
	}

	@Override
	public void setColor(int color) {
		int b = (color) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int r = (color >> 16) & 0xFF;
		CssColor cssColor = CssColor.make(r, g, b);
		currColor = new ColorGWT(r, g, b);
		c.setFillStyle(cssColor);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		c.fillRect(x, y, width, height);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		c.rect(x, y, width, height);
		c.stroke();

	}

	@Override
	public void setFont(telford.common.Font f) {
		currFont = f;
		c.setFont(f.toString());
	}

	@Override
	public telford.common.Font getFont() {
		return currFont;
	}

	@Override
	public void drawString(String str, int x, int y) {
		c.fillText(str, x, y);

	}

	@Override
	public telford.common.FontMetrics getFontMetrics(telford.common.Font f) {
		return new FontMetricsGWT(c);
	}

	@Override
	public int getColor() {
		return currColor.getBlue() << 16 | (currColor.getBlack() << 8) | currColor.getRed();
	}

	@Override
	public void fillOval(int x, int y, int height, int weight) {
		// scale context horizontally
		c.scale(1, height / weight);
		c.beginPath();
		// draw circle which will be stretched into an oval
		c.arc(x, y, weight, 0, 2 * Math.PI);
		// restore to original state
		c.restore();
		c.fill();
		c.stroke();
	}

	@Override
	public void draw(Line line) {
		c.beginPath();
		c.moveTo(line.p0.getX(), line.p0.getY());
		c.lineTo(line.p1.getX(), line.p1.getY());
		c.stroke();
	}
}