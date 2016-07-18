package tm.gwt.display;

import com.google.gwt.user.client.ui.Button;

import tm.gwt.jsInterface.GWTSuperTMFile;
import tm.gwt.jsInterface.MirrorState;
import tm.interfaces.CodeLineI ;
import tm.interfaces.SourceCoordsI ;
import tm.portableDisplays.CodeDisplayer;
import tm.portableDisplays.PortableContextInterface;

public class CodeDisplay1 extends DisplayAdapter {
	private GWTSuperTMFile theFile;
	MirrorState evaluator;
	PortableContextInterface context = new GWTContext();

	public CodeDisplay1(MirrorState e, PortableContextInterface context) {
		super(new CodeDisplayer(e, context), "codeDisplayPanel", "Test.java", 300, 600);
		this.evaluator = e;
		this.context = context;

		Button bBackup = new Button("<img src='/images/Backup.gif'/>");
		Button bStepOver = new Button("<img src='/images/stepOver.gif'/>");
		Button bStepInto = new Button("<img src='/images/stepInto.gif'/>");
		Button bStepOut = new Button("<img src='/images/stepOut.gif'/>");
		Button bToCursor = new Button("<img src='/images/ToCursor.gif'/>");
		Button bUpArrow = new Button("<img src='/images/UpArrow.gif'/>");
		Button bVW = new Button("<img src='/images/VW.gif'/>");
		Button bAutoStep = new Button("<img src='/images/AutoStep.gif'/>");

		toolBar.add(bBackup);
		toolBar.add(bStepOver);
		toolBar.add(bStepInto);
		toolBar.add(bStepOut);
		toolBar.add(bToCursor);
		toolBar.add(bUpArrow);
		toolBar.add(bVW);
		toolBar.add(bAutoStep);
		myWorkPane.setStyleName("tm-largeScrollPanel");
	}

	public void refresh() {
//		myWorkPane.setStyleName("tm-scrollPanel");
		if (displayer instanceof CodeDisplayer) {
			((CodeDisplayer) displayer).setDisplayInfo(((GWTContext) context).getCodeDisplayerInfo());
		}
		boolean allowGaps = true;
		setScale(1, 16);

		SourceCoordsI focus = evaluator.getCodeFocus();
		int focusLine = 0;
		boolean found = false;
		for (int sz = evaluator.getNumSelectedCodeLines(theFile, allowGaps); focusLine < sz; ++focusLine) {
			CodeLineI codeLine = evaluator.getSelectedCodeLine(theFile, allowGaps, focusLine);
			if (codeLine != null && codeLine.getCoords().equals(focus)) {
				found = true;
				break;
			}
		}
		if (found) {
			int topLine = (1 + focusLine) * getVScale();
			int bottomLine = topLine + getVScale();
			int vertValue = myWorkPane.getHorizontalScrollPosition();
			if (topLine < vertValue || bottomLine > vertValue + myWorkPane.getElement().getClientHeight()) {
				//should convert to PX unit
				myWorkPane.setVerticalScrollPosition(((topLine - 3 * getVScale())*300/600));
			}
		}
		super.refresh();
	}

}
