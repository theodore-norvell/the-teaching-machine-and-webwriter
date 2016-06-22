package tm.gwt.telford;

import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;

public class FontGWT implements telford.common.Font {
	String font;
	String name;
	int size;
	String style;

	public FontGWT() {
		this.name = "Monospaced";
		this.size = 1;
		this.style = FontStyle.NORMAL.getCssName();
	}

	public FontGWT(final String name, final int style, final int fontSize) {
		this.name = name;
		this.size = fontSize;
		this.style = getFontStyle(style);
	}

	public String getStyle() {
		return style;
	}

	public int getSize() {
		return size;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		if (font != null)
			return font;
		else
			return style + " " + size + Unit.PX.getType() + " " + name;
	}

	private String getFontStyle(int style) {
		String fontStyle = FontStyle.NORMAL.getCssName();
		;
		switch (style) {
		case 0:
			fontStyle = FontStyle.NORMAL.getCssName();// "plain";
			break;
		case 1:
			fontStyle = FontStyle.OBLIQUE.getCssName();// "bold";
			break;
		case 2:
			fontStyle = FontStyle.ITALIC.getCssName();// "italic";
			break;
		default:
			fontStyle = FontStyle.NORMAL.getCssName();
			;
			break;
		}
		return fontStyle;
	}
}
