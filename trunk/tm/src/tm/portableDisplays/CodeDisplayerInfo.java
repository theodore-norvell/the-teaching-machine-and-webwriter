package tm.portableDisplays;

import telford.common.Font;
import telford.common.Kit ;
import tm.interfaces.TMFileI ;

public class CodeDisplayerInfo {
	// Font styles
	private final static int PLAIN = 0;
	private final static int BOLD = 1;
	private final static int ITALIC = 2;

	private Font myFonts[] = { Kit.getKit().getFont("Monospaced", 0, 12),
	                           Kit.getKit().getFont("Monospaced", 1, 12),
	                           Kit.getKit().getFont("Monospaced", 2, 12),
	                           Kit.getKit().getFont("Monospaced", 3, 12) };
	private int fontMapper[] = { PLAIN, PLAIN, ITALIC, PLAIN, BOLD, PLAIN };
	private int fontColor[] = { 0x000000, 0x0000FF, 0x000000, 0xFF0000, 0x000000, 0xFF0000 };
	private int cursorColor = 0x008000;
	private int cursorLine = 0;
	private boolean lineNumbersCheckStatus = true;//show line number by default
	private TMFileI tmFile;
	private int tabSpaces = 4;
	private int delta_y = 12 ;
	private int focusLineNumber = 1;
	
	
	
	public int getFocusLineNumber() {
		return focusLineNumber;
	}

	public void setFocusLineNumber(int focusLineNumber) {
		this.focusLineNumber = focusLineNumber;
	}

	public TMFileI getTmFile() {
		return tmFile;
	}

	public void setTmFile(TMFileI tmFile) {
		this.tmFile = tmFile;
	}

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

	public int getDelta_y() {
		return this.delta_y;
	}

	public void setDelta_y( int newDelta_y) {
		this.delta_y = newDelta_y ;
	}
	
}
