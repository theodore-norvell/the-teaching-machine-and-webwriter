package tm.gwt.display;

import java.util.Vector ;

import tm.interfaces.CodeLineI ;
import tm.interfaces.MarkUpI ;
import tm.interfaces.SourceCoordsI ;

public class GWTCodeLineTemp implements CodeLineI {
	private char[] chars;
	private MarkUpI[] markUp;
	private int lineNum = 0;
	private SourceCoordsI coords;

	public GWTCodeLineTemp(StringBuffer buff, Vector<MarkUpI> markUpVector, SourceCoordsI sc) {
		chars = new char[buff.length()];
		if (buff.length() > 0)
			buff.getChars(0, buff.length(), chars, 0);

		markUp = new MarkUpI[markUpVector.size()];
		for (int i = 0, sz = markUpVector.size(); i < sz; ++i) {
			markUp[i] = markUpVector.elementAt(i);
		}
		coords = sc;
	}

	// NOT used for now
	public SourceCoordsI getCoords() {
		return coords;
	}

	public char[] getChars() {
		return chars;
	}

	public MarkUpI[] markUp() {
		return markUp;
	}

	/**
	 * Return true if the selection expression is valid anywhere on this line.
	 */
	public boolean isSelected() {
		return false;
	}

	public int getLineNumber() {
		return lineNum;
	}

	public void setLineNumber(int lineN) {
		lineNum = lineN;
	}

	public String toString() {
		String ch = new String(chars);
		StringBuffer mu = new StringBuffer("[");
		for (int i = 0; i < markUp.length; ++i) {
			if (i > 0)
				mu.append(", ");
			mu.append(markUp[i]);
		}
		mu.append("]");
		return "ClcCodeLine  <" + ch + ">, " + mu;
	}
}
