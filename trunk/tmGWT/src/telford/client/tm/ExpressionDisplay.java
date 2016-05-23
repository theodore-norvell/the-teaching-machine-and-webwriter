package telford.client.tm;

import java.util.Vector;

import telford.common.Font;
import telford.common.FontMetrics;
import telford.common.Graphics;
import telford.gwt.FontGWT;
import telford.gwt.GWTUtil;

public class ExpressionDisplay implements TMDisplayInterface {
	private String theExpression = "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ";// for
																							// test
	// Simply copied from TM.ExpressionDisplay.
	private final int LEFTMARGIN = 4;
	private final int TOPMARGIN = 20;

	public String getExpression() {
		return theExpression;
	}

	public void setExpression(String exp) {
		theExpression = exp;
	}

	public String refresh(EvaluatorInterface evaluator) {
		theExpression = evaluator.getExpression();
		return theExpression;
	}

	public void drawArea(Graphics tg) {
		int advance = LEFTMARGIN;
		Font f = new FontGWT("normal 16px serif");
		tg.setFont(f);

		if (theExpression.length() > 0) {
			char tempArray[] = theExpression.toCharArray();
			tg.setColor(0); // black
			int currColor = 0;
			boolean currUnderline = false;
			int currWidth = 0;
			int baseline = TOPMARGIN + 12;
			Vector<Object> attrStack = new Vector<>();
			for (int i = 0, sz = theExpression.length(); i < sz; ++i) {
				char c = tempArray[i]; // Next character
				switch (c) {
				case GWTUtil.MARKER1:
					attrStack.addElement("red");
					currColor = 0xFF;
					tg.setColor(currColor);
					break;
				case GWTUtil.MARKER2:
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = false;
					break;
				case GWTUtil.MARKER3:
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = true;
					break;
				case GWTUtil.MARKER4:
					attrStack.addElement("blue");
					currColor = 0xFF0000;
					tg.setColor(currColor);
					break;
				case GWTUtil.ENDMARKER:
					Object temp = attrStack.elementAt(attrStack.size() - 1);
					if (temp instanceof String) {
						currColor = temp.equals("red") ? 0xFF : 0xFF0000;
						tg.setColor(currColor);
					} else {
						currUnderline = ((Boolean) temp).booleanValue();
					}
					attrStack.removeElementAt(attrStack.size() - 1);
					break;
				default:
					FontMetrics fm = tg.getFontMetrics(f);
					currWidth = fm.stringWidth(String.valueOf(tempArray[i]));
					tg.drawString(String.valueOf(tempArray[i]), advance, baseline);
					if (currUnderline) {
						tg.setColor(0);
						tg.fillRect(advance, baseline + 2, currWidth, +3);
						tg.setColor(currColor);
					}
					advance += currWidth;// advances[c];
				}
			}
		}
	}

	public ExpressionDisplay() {
	}
}