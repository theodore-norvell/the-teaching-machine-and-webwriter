package tm.gwt.display;

import com.google.gwt.core.client.js.JsProperty;

import tm.portableDisplaysGWT.CodeLine;
import tm.portableDisplaysGWT.StateInterface;

//@JsType
public interface MirrorState extends StateInterface{
	@JsProperty
	public String getExpression();
	public CodeLine getSelectedCodeLine(boolean allowGaps, int i);
	public int getNumSelectedCodeLines(boolean allowGaps);
}
