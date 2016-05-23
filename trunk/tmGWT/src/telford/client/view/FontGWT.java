package telford.client.view;

public class FontGWT implements telford.common.Font {
	String font;
	String fontFamily;
	int fontSize;
	String text;
	public FontGWT(String f) {
		this.font = f;
	}
	
	public FontGWT(final String fontFamily, final int fontSize,
            final String text) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.text = text;
    }

	public String toString() {
		if(font != null)
			return font;
		else
			return fontFamily + " " + fontSize;
	}
}