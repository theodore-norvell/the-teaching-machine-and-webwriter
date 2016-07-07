package tm.gwt.display;

import telford.common.Font;
import telford.common.Kit;
import tm.portableDisplays.PortableContextInterface;
import tm.gwt.jsInterface.MirrorState;
import tm.portableDisplays.CodeDisplayer;

public class CodeDisplayCanvas extends CodeDisplayer {
	MirrorState evaluator;
	

	public CodeDisplayCanvas(MirrorState evaluator, PortableContextInterface context) {
		super(evaluator, context);
		
		//TODO should coming from server side
		this.getDisplayInfo().setCursorColor(65280);
		this.getDisplayInfo().setCursorLine(0);
		this.getDisplayInfo().setFocusLineNumber(27);
		int fontMapper[] = { 0, 1, 2, 1, 1, 0 };
		int fontColor[] = {0, 10040115, 37440, 10040115, 2752767, 16711680};
		Font f1 = Kit.getKit().getFont("Monospaced", 0, 12);
		Font f2 = Kit.getKit().getFont("Monospaced", 1, 12);
		Font f3 = Kit.getKit().getFont("Monospaced", 2, 12);
		Font f4 = Kit.getKit().getFont("Monospaced", 3, 12);
		this.getDisplayInfo().setMyFontByIndex(f1, 0);
		this.getDisplayInfo().setMyFontByIndex(f2, 1);
		this.getDisplayInfo().setMyFontByIndex(f3, 2);
		this.getDisplayInfo().setMyFontByIndex(f4, 3);
		this.getDisplayInfo().setFontMapper(fontMapper);
		this.getDisplayInfo().setFontColor(fontColor);
	}
}
