package telford.common;

public class Rectangle {
	public int x;

	public int y;

	public int width;

	public int height;

	public Rectangle() {
		this(0, 0, 0, 0);
	}

	public Rectangle(Rectangle r) {
		this(r.x, r.y, r.width, r.height);
	}

	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle(int width, int height) {
		this(0, 0, width, height);
	}

	public Rectangle(Point p, Dimension d) {
		this((int)p.x, (int)p.y, d.width, d.height);
	}

	public Rectangle(Point p) {
		this((int)p.x, (int)p.y, 0, 0);
	}

	public Rectangle(Dimension d) {
		this(0, 0, d.width, d.height);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	 public boolean contains(Point p) {
	        return inside((int)p.x, (int)p.y);
	    }
	
	public boolean inside(int X, int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X) &&
                (h < y || h > Y));
    }
}
