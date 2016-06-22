package tm.portableDisplaysGWT;

import telford.common.Font;

public class CodeDisplayerInfo {
	// Font styles
	private final static int PLAIN = 0;
	private final static int BOLD = 1;
	private final static int ITALIC = 2;

	private Font myFonts[] = { null, null, null, null }; // Indexed by font
	private int fontMapper[] = { PLAIN, PLAIN, ITALIC, PLAIN, BOLD, PLAIN };
	private int fontColor[] = { 0x000000, 0x0000FF, 0x000000, 0xFF0000, 0x000000, 0xFF0000 };
	private int cursorColor = 0x008000;
	private int cursorLine = 0;
	private boolean lineNumbersCheckStatus = true;//show line number by default
	public int getCursorLine() {
		return cursorLine;
	}

	public void setCursorLine(int cursorLine) {
		this.cursorLine = cursorLine;
	}

	
	public boolean getLineNumbersCheckStatus() {
		return lineNumbersCheckStatus;
	}

	public void setLineNumbersCheckStatus(boolean lineNumbersCheckStatus) {
		this.lineNumbersCheckStatus = lineNumbersCheckStatus;
	}

	public Font[] getMyFonts() {
		return myFonts;
	}

	public void setMyFonts(Font[] myFonts) {
		this.myFonts = myFonts;
	}

	public int[] getFontMapper() {
		return fontMapper;
	}

	public void setFontMapper(int[] fontMapper) {
		this.fontMapper = fontMapper;
	}

	public int[] getFontColor() {
		return fontColor;
	}

	public void setFontColor(int[] fontColor) {
		this.fontColor = fontColor;
	}

	public int getCursorColor() {
		return cursorColor;
	}

	public void setCursorColor(int cursorColor) {
		this.cursorColor = cursorColor;
	}

	private int tabSpaces = 4;

	public int getTabSpaces() {
		return tabSpaces;
	}

	public void setTabSpaces(int tabSpaces) {
		this.tabSpaces = tabSpaces;
	}
	
	//============methods by index
	public Font getMyFontByIndex(int index) {
		return myFonts[index];
	}

	public void setMyFontByIndex(Font myFont, int index) {
		this.myFonts[index] = myFont;
	}

	public int getFontMapperByIndex(int index) {
		return fontMapper[index];
	}

	public void setFontMapperByIndex(int fontMapper, int index) {
		this.fontMapper[index] = fontMapper;
	}

	public int getFontColorByIndex(int index) {
		return fontColor[index];
	}

	public void setFontColorByIndex(int fontColor, int index) {
		this.fontColor[index] = fontColor;
	}
	
}
