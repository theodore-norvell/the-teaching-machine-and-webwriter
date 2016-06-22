package tm.portableDisplays;

import telford.common.Font;
import telford.common.FontMetrics;
import telford.common.Graphics;
import telford.jse.FontJSE;
import tm.interfaces.CodeLine;
import tm.interfaces.EvaluatorInterface;
import tm.interfaces.MarkUp;
import tm.interfaces.SelectionInterface;
import tm.interfaces.SourceCoords;
import tm.portableDisplaysGWT.CodeDisplayerInfo;
import tm.portableDisplaysGWT.PortableContextInterface;
import tm.utilities.Assert;
import tm.utilities.TMFile;
import tm.virtualMachine.TagSet;

public class CodeDisplayer extends PortableDisplayer {
	public CodeDisplayer(EvaluatorInterface model, PortableContextInterface context) {
		super(model, context);
	}

	private final static int LEFT_MARGIN = 10; // These units are in pixels
	private final static int LINE_PADDING = 1; // Space between lines

	// Printing modes
	private final static int NORMAL = 0;
	private final static int KEYWORD = 1;
	private final static int COMMENT = 2;
	private final static int PREPROCESSOR = 3;
	private final static int CONSTANT = 4;
	private final static int LINE_NUMBER = 5;
	private final static int BOLD = 1;
	private final static int ITALIC = 2;
	private final static int BOLD_ITALIC = 3;
	private CodeDisplayerInfo displayInfo = new CodeDisplayerInfo();

	public CodeDisplayerInfo getDisplayInfo() {
		return displayInfo;
	}
	public void setDisplayInfo(CodeDisplayerInfo displayInfo) {
		this.displayInfo = displayInfo;
	}

	private TMFile theFile = null; // The file currently being displayed.

	@Override
	public void refresh() {
	}

	@Override
	public void paintComponent(Graphics g) {
		drawArea(g);
	}

	public void drawArea(Graphics screen) {
		setMode(screen, NORMAL); // Always start in normal mode
		FontMetrics fm = screen.getFontMetrics(screen.getFont());

		final int lineHeight = fm.getHeight();
		int baseLine = lineHeight + LINE_PADDING;

		SourceCoords focus = model.getCodeFocus();
		/*
		 * DBG System.out.println("Current file is " + file.getFileName());/*DBG
		 */
		if (theFile == null)
			theFile = focus.getFile();
		boolean allowGaps = displayInfo.getLineNumbersCheckStatus();
		int n = model.getNumSelectedCodeLines(theFile, allowGaps);
		for (int i = 0; i < n; i++) {
			baseLine += lineHeight + LINE_PADDING;
			CodeLine theLine = model.getSelectedCodeLine(theFile, allowGaps, i);
			if (theLine != null && theLine.getCoords().equals(focus)) {
				int save = screen.getColor();
				screen.setColor(context.getHighlightColor());
				screen.fillRect(0, baseLine - fm.getAscent(), getWidth(), fm.getAscent() + fm.getDescent());
				screen.setColor(save);
			}
			if (displayInfo.getCursorLine() == i) {
				int save = screen.getColor();
				screen.setColor(displayInfo.getCursorColor());
				screen.fillRect(0, baseLine - fm.getAscent(), 10, fm.getAscent() + fm.getDescent());
				screen.setColor(save);
				// Update the cursorLineCoords
//				if (theLine == null)
//					cursorLineCoords = null;
//				else
//					cursorLineCoords = theLine.getCoords();
			}
			drawLine(theLine, LEFT_MARGIN - 0, baseLine - 0, screen);
		}
	}

	// Change the current font colouring/style mode
	private void setMode(Graphics screen, int newMode) {

		if (displayInfo.getMyFontByIndex(0) != context.getCodeFont()) {
			displayInfo.setMyFontByIndex(context.getCodeFont(),0);
			setSecondaryFonts(displayInfo.getMyFontByIndex(0));
		}

		screen.setFont(displayInfo.getMyFontByIndex(displayInfo.getFontMapperByIndex(newMode)));
		screen.setColor(displayInfo.getFontColorByIndex(newMode));
	}

	// Creates the style variations on the current font
	private void setSecondaryFonts(Font primary) {
		displayInfo.setMyFontByIndex(new FontJSE(primary.getName(), BOLD, primary.getSize()), BOLD);
		displayInfo.setMyFontByIndex(new FontJSE(primary.getName(), ITALIC, primary.getSize()), ITALIC);
		displayInfo.setMyFontByIndex(new FontJSE(primary.getName(), BOLD_ITALIC, primary.getSize()), BOLD_ITALIC);
	}

	private void drawLine(CodeLine codeLine, int x, int y, Graphics screen) {
		// System.out.println( "Displaying "+codeLine );
		setMode(screen, NORMAL);
		FontMetrics fm = screen.getFontMetrics(screen.getFont());
		final int em = fm.stringWidth("M"); 
		if (codeLine == null) {
			// A null represents a gap in the selection. We draw three dots.
			fm = screen.getFontMetrics(screen.getFont());
			screen.drawString("...", x, y);
			return;
		}
		if(displayInfo.getLineNumbersCheckStatus()){
			// 5 characters and a colon.
			int lineNum = codeLine.getCoords().getLineNumber();
			char[] lineNumArray = new char[] { ' ', ' ', ' ', ' ', ' ', ':' };
			for (int i = 0; i < 5; ++i) {
				lineNumArray[4 - i] = (char) ('0' + lineNum % 10);
				lineNum /= 10;
				if (lineNum == 0)
					break;
			}
			setMode(screen, LINE_NUMBER);
			fm = screen.getFontMetrics(screen.getFont());
			String strTemp ="";
			for (int i = 0; i < 6; i++) {
				strTemp = strTemp.concat(String.valueOf(lineNumArray[i]));
			}
			screen.drawString(strTemp, x, y);
			x += 7 * em; 
		}

		int column = 0;
		int m = 0; // index into the markup array
		char[] chars = codeLine.getChars();
		MarkUp[] markUp = codeLine.markUp();
		setMode(screen, NORMAL);
		fm = screen.getFontMetrics(screen.getFont());
		SelectionInterface selection = model.getSelection();
		boolean visible = TagSet.EMPTY.selectionIsValid(selection);
		for (int i = 0, sz = chars.length; i < sz; ++i) {
			// Process all MarkUp commands that apply up to column i.
			while (m < markUp.length && markUp[m].column <= i) {
				int command = markUp[m].command;
				switch (command) {
				case MarkUp.NORMAL: {
					setMode(screen, NORMAL);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUp.KEYWORD: {
					setMode(screen, KEYWORD);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUp.COMMENT: {
					setMode(screen, COMMENT);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUp.PREPROCESSOR: {
					setMode(screen, PREPROCESSOR);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUp.CONSTANT: {
					setMode(screen, CONSTANT);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUp.CHANGE_TAG_SET: {
					visible = markUp[m].tagSet.selectionIsValid(selection);
				}
					break;
				default: {
					Assert.check(false);
				}
				}
				m++;
			}

			// Now display the character.
			if (visible) {
				if (chars[i] == '\t') {
					// Expand the tabs
					// column 0 1 2 3 4 5 6 7 8 9 10 11 12 13
					// newcolumn 4 4 4 4 8 8 8 8 12 12 12 12 16
					int newColumn = (column / displayInfo.getTabSpaces() + 1) * displayInfo.getTabSpaces();
					Assert.check(newColumn > column);
					char[] space = new char[] { ' ' };
					
					x += (newColumn - column) * fm.stringWidth(String.valueOf(space[0]));
					column = newColumn;
				} else {
					// A printable character, we hope!
					column += 1;
					screen.drawString(String.valueOf(chars[i]), x, y);
					x +=  fm.stringWidth(String.valueOf(chars[i]));
				}
			}
		}
	}
}
