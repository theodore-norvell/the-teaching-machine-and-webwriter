package tm.portableDisplays;

import telford.common.Font;
import telford.common.FontMetrics;
import telford.common.Graphics;
import telford.common.Kit;
import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUpI ;
import tm.interfaces.SelectionInterface ;
import tm.interfaces.SourceCoordsI ;
import tm.interfaces.StateInterface ;
import tm.interfaces.TMFileI ;

public class CodeDisplayer extends PortableDisplayer {
	public CodeDisplayer(StateInterface model, PortableContextInterface context) {
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

	private TMFileI theFile = null;

	@Override
	public void refresh() {
	}

	@Override
	public void paintComponent(Graphics screen) {
		setMode(screen, NORMAL); // Always start in normal mode
		FontMetrics fm = screen.getFontMetrics(screen.getFont());

		final int lineHeight = fm.getHeight();
		int baseLine = lineHeight + LINE_PADDING;
		if (theFile == null)
			theFile = displayInfo.getTmFile();
		boolean allowGaps = displayInfo.getLineNumbersCheckStatus();
		int n = model.getNumSelectedCodeLines(theFile, allowGaps);
		SourceCoordsI focus = model.getCodeFocus();
		for (int i = 0; i < n; i++) {
			baseLine += lineHeight + LINE_PADDING;
			CodeLineI theLine = model.getSelectedCodeLine(theFile, allowGaps, i);
			if (theLine != null && theLine.getCoords().equals( focus )) {
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
			}
			drawLine(theLine, LEFT_MARGIN - 0, baseLine - 0, screen);
		}
	}

	// Change the current font colouring/style mode
	private void setMode(Graphics screen, int newMode) {

		if (displayInfo.getMyFontByIndex(0) != context.getCodeFont()) {
			displayInfo.setMyFontByIndex(context.getCodeFont(), 0);
			setSecondaryFonts(displayInfo.getMyFontByIndex(0));
		}

		screen.setFont(displayInfo.getMyFontByIndex(displayInfo.getFontMapperByIndex(newMode)));
		screen.setColor(displayInfo.getFontColorByIndex(newMode));
	}

	// Creates the style variations on the current font
	private void setSecondaryFonts(Font primary) {
		displayInfo.setMyFontByIndex(Kit.getKit().getFont(primary.getName(), BOLD, primary.getSize()), BOLD);
		displayInfo.setMyFontByIndex(Kit.getKit().getFont(primary.getName(), ITALIC, primary.getSize()), ITALIC);
		displayInfo.setMyFontByIndex(Kit.getKit().getFont(primary.getName(), BOLD_ITALIC, primary.getSize()),
				BOLD_ITALIC);
	}

	private void drawLine(CodeLineI codeLine, int x, int y, Graphics screen) {
		setMode(screen, NORMAL);
		FontMetrics fm = screen.getFontMetrics(screen.getFont());
		final int em = fm.stringWidth("M");
		if (codeLine == null) {
			// A null represents a gap in the selection. We draw three dots.
			fm = screen.getFontMetrics(screen.getFont());
			screen.drawString("...", x, y);
			return;
		}
		if (displayInfo.getLineNumbersCheckStatus()) {
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
			String strTemp = "";
			for (int i = 0; i < 6; i++) {
				strTemp = strTemp.concat(String.valueOf(lineNumArray[i]));
			}
			screen.drawString(strTemp, x, y);
			x += 7 * em;
		}

		int column = 0;
		int m = 0; // index into the markup array
		char[] chars = codeLine.getChars();
		MarkUpI[] markUp = codeLine.markUp();
		setMode(screen, NORMAL);
		fm = screen.getFontMetrics(screen.getFont());
		SelectionInterface selection = model.getSelection();
		boolean visible = selection.isValidForEmptyTagSet();
		for (int i = 0, sz = chars.length; i < sz; ++i) {
			// Process all MarkUp commands that apply up to column i.
			while (m < markUp.length && markUp[m].getColumn() <= i) {
				int command = markUp[m].getCommand();
				switch (command) {
				case MarkUpI.NORMAL: {
					setMode(screen, NORMAL);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUpI.KEYWORD: {
					setMode(screen, KEYWORD);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUpI.COMMENT: {
					setMode(screen, COMMENT);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUpI.PREPROCESSOR: {
					setMode(screen, PREPROCESSOR);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUpI.CONSTANT: {
					setMode(screen, CONSTANT);
					fm = screen.getFontMetrics(screen.getFont());
				}
					break;
				case MarkUpI.CHANGE_TAG_SET: {
					visible = markUp[m].getTagSet().selectionIsValid(selection);
				}
					break;
				default: {
					context.getAsserter().check("Unreachable code reached.");
				}
				}
				m++;
			}

			// Now display the character.
			if (visible) {
				if (chars[i] == '\t') {
					int newColumn = (column / displayInfo.getTabSpaces() + 1) * displayInfo.getTabSpaces();
					context.getAsserter().check(newColumn > column);
					char[] space = new char[] { ' ' };

					x += (newColumn - column) * fm.stringWidth(String.valueOf(space[0]));
					column = newColumn;
				} else {
					// A printable character, we hope!
					column += 1;
					screen.drawString(String.valueOf(chars[i]), x, y);
					x += fm.stringWidth(String.valueOf(chars[i]));
				}
			}
		}
	}
}
