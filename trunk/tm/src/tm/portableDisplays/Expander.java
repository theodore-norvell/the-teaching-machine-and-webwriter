package tm.portableDisplays;

import telford.common.Graphics;
import telford.common.Line;
import telford.common.Point;
import telford.common.Rectangle;

public class Expander {

	/*
	 * Expander box dimensions:
	 */
	public static final int EXPAND_X = 2;
	public static final int EXPAND_Y = EXPAND_X;
	public static final int EXPAND_H = DatumDisplay.baseHeight - 2 * EXPAND_Y;
	public static final int EXPAND_W = EXPAND_H;
	public static final int EXPAND_OFFSET = EXPAND_W + 2 * EXPAND_X;

	private boolean isExpanded;

	/*
	 * The extent of the expander box relative to parent's drawing co-ordinates.
	 */
	Rectangle extent;

	public Expander() {
		isExpanded = false;
		extent = new Rectangle(0, 0, EXPAND_H, EXPAND_W);
	}

	public boolean getExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean on) {
		isExpanded = on;
	}

	public void move(int x, int y) {
		extent.x = x;
		extent.y = y;
	}

	public void shift(int dx, int dy) {
		extent.x += dx;
		extent.y += dy;
	}

	public boolean contains(Point p, Point location) {
		extent.x += location.getX();
		extent.y += location.getY();
		boolean does = extent.contains(p);
		extent.x -= location.getX();
		extent.y -= location.getY();
		return does;
	}

	public void draw(Graphics screen, Rectangle ownerPlace) {
		if (extent == null)
			return;
		// Expand box extent
		int x = ownerPlace.x + extent.x;
		int y = ownerPlace.y + extent.y;
		int w = extent.width;
		int h = extent.height;

		screen.drawRect(x, y, w, h);
		if (isExpanded) {
			StringBox.paintString("-", x + w / 2, y + h / 2, screen, StringBox.CENTRE, StringBox.MIDDLE);
		} else
			StringBox.paintString("+", x + w / 2, y + h / 2, screen, StringBox.CENTRE, StringBox.MIDDLE);
	}

	public void drawNubbin(Graphics screen, Rectangle ownerPlace, boolean vert) {
		if (extent == null)
			return;
		// Expand box extent
		int x = ownerPlace.x + extent.x;
		int y = ownerPlace.y + extent.y;
		// Horizontal nubbin from right edge of expander box to end of expander
		// area
		Line line = new Line(new Point(x + extent.width, y + extent.height / 2),
				new Point(x + EXPAND_OFFSET, y + extent.height / 2));
		screen.draw(line);
		if (isExpanded && vert) {
			x += extent.width / 2;
			line = new Line(new Point(x, y + extent.height), new Point(x, y + DatumDisplay.baseHeight));
			screen.draw(line);
		}
	}
}
