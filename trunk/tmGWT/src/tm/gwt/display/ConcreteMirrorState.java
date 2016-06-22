package tm.gwt.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import tm.portableDisplaysGWT.CodeLine;
import tm.portableDisplaysGWT.MarkUp;

public class ConcreteMirrorState implements MirrorState {
	private List<CodeLine> lines = null;

	public ConcreteMirrorState() {
		parserSourceFile();
	}

	public String getExpression() {
		return "";
	}

	public CodeLine getSelectedCodeLine(boolean allowGaps, int index) {
		CodeLine c = lines.get(index);
		return c;
	};

	public int getNumSelectedCodeLines(boolean allowGaps) {
		return lines.size();
	}

	/* TODO CodeLines should come from StateInterface */
	private void parserSourceFile() {
		lines = new ArrayList<CodeLine>();
		StringBuffer sb0 = new StringBuffer("package test;");
		Vector<MarkUp> markUp0 = new Vector<MarkUp>();
		markUp0.add(0, new MarkUp(0, MarkUp.KEYWORD));
		markUp0.add(1, new MarkUp(7, MarkUp.NORMAL));
		CodeLine cl0 = new CodeLine(sb0, markUp0);
		cl0.setLineNumber(1);
		lines.add(0, cl0);
		
		StringBuffer sb1 = new StringBuffer("");
		Vector<MarkUp> markUp1 = new Vector<MarkUp>();
		CodeLine cl1 = new CodeLine(sb1, markUp1);
		cl1.setLineNumber(2);
		lines.add(1, cl1);
		
		StringBuffer sb2 = new StringBuffer("public class Test {");
		Vector<MarkUp> markUp2 = new Vector<MarkUp>();
		markUp2.add(0, new MarkUp(0, MarkUp.KEYWORD));
		markUp2.add(1, new MarkUp(6, MarkUp.NORMAL));
		markUp2.add(2, new MarkUp(7, MarkUp.KEYWORD));
		markUp2.add(3, new MarkUp(12, MarkUp.NORMAL));
		CodeLine cl2 = new CodeLine(sb2, markUp2);
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
		CodeLine cl3 = new CodeLine(sb3, markUp3);
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
		CodeLine cl4 = new CodeLine(sb4, markUp4);
		cl4.setLineNumber(5);
		lines.add(4, cl4);
		
		StringBuffer sb5 = new StringBuffer("	int j=2;");
		Vector<MarkUp> markUp5 = new Vector<MarkUp>();
		markUp5.add(0, new MarkUp(1, MarkUp.KEYWORD));
		markUp5.add(1, new MarkUp(4, MarkUp.NORMAL));
		markUp5.add(2, new MarkUp(9, MarkUp.CONSTANT));
		markUp5.add(3, new MarkUp(10, MarkUp.NORMAL));
		markUp5.add(4, new MarkUp(11, MarkUp.COMMENT));
		markUp5.add(5, new MarkUp(25, MarkUp.NORMAL));
		CodeLine cl5 = new CodeLine(sb5, markUp5);
		cl5.setLineNumber(6);
		lines.add(5, cl5);
		
		
		StringBuffer sb6 = new StringBuffer("	int r = i + j;");
		Vector<MarkUp> markUp6 = new Vector<MarkUp>();
		markUp6.add(0, new MarkUp(1, MarkUp.KEYWORD));
		markUp6.add(1, new MarkUp(4, MarkUp.NORMAL));
		CodeLine cl6 = new CodeLine(sb6, markUp6);
		cl6.setLineNumber(7);
		lines.add(6, cl6);
		
		StringBuffer sb7 = new StringBuffer("	System.out.println(r);");
		Vector<MarkUp> markUp7 = new Vector<MarkUp>();
		CodeLine cl7 = new CodeLine(sb7, markUp7);
		cl7.setLineNumber(8);
		lines.add(7, cl7);
		
		StringBuffer sb8 = new StringBuffer("}");
		Vector<MarkUp> markUp8 = new Vector<MarkUp>();
		CodeLine cl8 = new CodeLine(sb8, markUp8);
		cl8.setLineNumber(9);
		lines.add(8, cl8);
		
		StringBuffer sb9 = new StringBuffer("}");
		Vector<MarkUp> markUp9 = new Vector<MarkUp>();
		CodeLine cl9 = new CodeLine(sb9, markUp9);
		cl9.setLineNumber(10);
		lines.add(9, cl9);
	}
}
