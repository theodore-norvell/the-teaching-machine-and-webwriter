package tm.gwt.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import tm.gwt.jsInterface.MirrorState;
import tm.portableDisplays.CodeLine;
import tm.portableDisplays.MarkUp;
import tm.portableDisplays.SelectionInterface;
import tm.portableDisplays.SuperSourceCoords;
import tm.portableDisplays.SuperTMFile;

public class ConcreteMirrorState implements MirrorState {
	private List<GWTCodeLineTemp> lines = null;
	private SelectionInterface selection;
	private SuperSourceCoords focus;//TODO temporarily for test 

	public ConcreteMirrorState(SelectionInterface sc) {
		this.selection = sc;
		focus = new GWTSourceCoords(27);
		parserSourceFile();
	}

	public String getExpression() {
		return "";
	}

	public SuperSourceCoords getCodeFocus() {
		return focus;
	}

	public CodeLine getSelectedCodeLine(SuperTMFile tmFile, boolean allowGaps, int index) {
		CodeLine c = lines.get(index);
		return c;
	};

	public int getNumSelectedCodeLines(SuperTMFile tmFile, boolean allowGaps) {
		return lines.size();
	}

	public void setSelection(SelectionInterface selection) {
		this.selection = selection;
	}

	public SelectionInterface getSelection() {
		return selection;
	};

	/* CodeLines should come from StateInterface */
	private void parserSourceFile() {
		lines = new ArrayList<GWTCodeLineTemp>();
		StringBuffer sb0 = new StringBuffer("package test;");
		Vector<MarkUp> markUp0 = new Vector<MarkUp>();
		markUp0.add(0, new MarkUp(0, MarkUp.KEYWORD));
		markUp0.add(1, new MarkUp(7, MarkUp.NORMAL));
		GWTCodeLineTemp cl0 = new GWTCodeLineTemp(sb0, markUp0, new GWTSourceCoords(1));
		cl0.setLineNumber(1);
		lines.add(0, cl0);

		StringBuffer sb1 = new StringBuffer("");
		Vector<MarkUp> markUp1 = new Vector<MarkUp>();
		GWTCodeLineTemp cl1 = new GWTCodeLineTemp(sb1, markUp1, new GWTSourceCoords(2));
		cl1.setLineNumber(2);
		lines.add(1, cl1);

		StringBuffer sb2 = new StringBuffer("public class Test {");
		Vector<MarkUp> markUp2 = new Vector<MarkUp>();
		markUp2.add(0, new MarkUp(0, MarkUp.KEYWORD));
		markUp2.add(1, new MarkUp(6, MarkUp.NORMAL));
		markUp2.add(2, new MarkUp(7, MarkUp.KEYWORD));
		markUp2.add(3, new MarkUp(12, MarkUp.NORMAL));
		GWTCodeLineTemp cl2 = new GWTCodeLineTemp(sb2, markUp2, new GWTSourceCoords(3));
		cl2.setLineNumber(3);
		lines.add(2, cl2);

		StringBuffer sb3 = new StringBuffer("public static void main(String[] args) {");
		Vector<MarkUp> markUp3 = new Vector<MarkUp>();
		markUp3.add(0, new MarkUp(0, MarkUp.KEYWORD));
		markUp3.add(1, new MarkUp(6, MarkUp.NORMAL));
		markUp3.add(2, new MarkUp(7, MarkUp.KEYWORD));
		markUp3.add(3, new MarkUp(13, MarkUp.NORMAL));
		markUp3.add(4, new MarkUp(14, MarkUp.KEYWORD));
		markUp3.add(5, new MarkUp(18, MarkUp.NORMAL));
		GWTCodeLineTemp cl3 = new GWTCodeLineTemp(sb3, markUp3, new GWTSourceCoords(4));
		cl3.setLineNumber(4);
		lines.add(3, cl3);

		StringBuffer sb4 = new StringBuffer("	int i = 1;//test comment");
		Vector<MarkUp> markUp4 = new Vector<MarkUp>();
		markUp4.add(0, new MarkUp(1, MarkUp.KEYWORD));
		markUp4.add(1, new MarkUp(4, MarkUp.NORMAL));
		markUp4.add(2, new MarkUp(9, MarkUp.CONSTANT));
		markUp4.add(3, new MarkUp(10, MarkUp.NORMAL));
		markUp4.add(4, new MarkUp(11, MarkUp.COMMENT));
		markUp4.add(5, new MarkUp(25, MarkUp.NORMAL));
		GWTCodeLineTemp cl4 = new GWTCodeLineTemp(sb4, markUp4, new GWTSourceCoords(5));
		cl4.setLineNumber(5);
		lines.add(4, cl4);

		StringBuffer sb5 = new StringBuffer("	int j=2;");
		Vector<MarkUp> markUp5 = new Vector<MarkUp>();
		markUp5.add(0, new MarkUp(1, MarkUp.KEYWORD));
		markUp5.add(1, new MarkUp(4, MarkUp.NORMAL));
		markUp5.add(2, new MarkUp(7, MarkUp.CONSTANT));
		markUp5.add(3, new MarkUp(8, MarkUp.NORMAL));
		GWTCodeLineTemp cl5 = new GWTCodeLineTemp(sb5, markUp5, new GWTSourceCoords(6));
		cl5.setLineNumber(6);
		lines.add(5, cl5);

		for (int i = 6; i < 36; i++) {
			addNewLine(lines, i);
		}

		StringBuffer sb36 = new StringBuffer("	int r = i + j;");
		Vector<MarkUp> markUp36 = new Vector<MarkUp>();
		markUp36.add(0, new MarkUp(1, MarkUp.KEYWORD));
		markUp36.add(1, new MarkUp(4, MarkUp.NORMAL));
		GWTCodeLineTemp cl36 = new GWTCodeLineTemp(sb36, markUp36, new GWTSourceCoords(37));
		cl36.setLineNumber(37);
		lines.add(36, cl36);

		StringBuffer sb37 = new StringBuffer("	System.out.println(r);");
		Vector<MarkUp> markUp37 = new Vector<MarkUp>();
		GWTCodeLineTemp cl37 = new GWTCodeLineTemp(sb37, markUp37, new GWTSourceCoords(38));
		cl37.setLineNumber(38);
		lines.add(37, cl37);

		StringBuffer sb38 = new StringBuffer("}");
		Vector<MarkUp> markUp38 = new Vector<MarkUp>();
		GWTCodeLineTemp cl38 = new GWTCodeLineTemp(sb38, markUp38, new GWTSourceCoords(39));
		cl38.setLineNumber(39);
		lines.add(38, cl38);

		StringBuffer sb39 = new StringBuffer("}");
		Vector<MarkUp> markUp39 = new Vector<MarkUp>();
		GWTCodeLineTemp c39 = new GWTCodeLineTemp(sb39, markUp39, new GWTSourceCoords(40));
		c39.setLineNumber(40);
		lines.add(39, c39);
	}

	private void addNewLine(List<GWTCodeLineTemp> lines, int i) {
		// String index = String.valueOf(i-5);
		StringBuffer sb = new StringBuffer("	int j=2;");
		Vector<MarkUp> markUp = new Vector<MarkUp>();
		markUp.add(0, new MarkUp(1, MarkUp.KEYWORD));
		markUp.add(1, new MarkUp(4, MarkUp.NORMAL));
		markUp.add(2, new MarkUp(7, MarkUp.CONSTANT));
		markUp.add(3, new MarkUp(8, MarkUp.NORMAL));
		GWTCodeLineTemp cl = new GWTCodeLineTemp(sb, markUp, new GWTSourceCoords(i + 1));
		cl.setLineNumber(i + 1);
		lines.add(i, cl);
	}
}
