package tm.portableDisplaysGWT;

import java.util.Vector;

public class CodeLine {
	private char[] chars = { 'p', 'a', 'c', 'k', 'a', 'g', 'e', ' ', 't', 'e', 's', 't', ';' };
	private MarkUp[] markUp;
	private int lineNum = 0;

	public CodeLine( StringBuffer buff, Vector<MarkUp> markUpVector) {
        chars = new char[ buff.length() ] ;
        if( buff.length() > 0 ) 
            buff.getChars(0, buff.length(), chars, 0);

        markUp = new MarkUp[ markUpVector.size() ] ;
        for( int i = 0, sz = markUpVector.size() ; i < sz ; ++i ) {
            markUp[i] = markUpVector.elementAt(i) ; }
    }
	
	public char[] getChars() {
		return chars;
	}

	public MarkUp[] markUp() {
		//value should come from file, for now it is only used for test
//		markUp = new MarkUp[2];
//		markUp[0] = new MarkUp(0, MarkUp.KEYWORD);
//		markUp[1] = new MarkUp(7, MarkUp.NORMAL);
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
