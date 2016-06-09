package telford.jse;

import java.awt.Font;

public class FontJSE implements telford.common.Font {
	Font f;

	FontJSE(Font f) {
		this.f = f;
	}
	public FontJSE(String name, int style, int size) {
		this.f = new Font(name, style, size);
	}
	
	public String getName() {
		return f.getName();
	}

	public int getSize() {
		return f.getSize();
	}

}
