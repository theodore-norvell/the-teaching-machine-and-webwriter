package tm.gwt.jsInterface;

import com.google.gwt.core.client.js.JsProperty;

import tm.interfaces.StateInterface ;

//@JsType
public interface MirrorState extends StateInterface{
	@JsProperty
	public String getExpression();
//	public CodeLine getSelectedCodeLine(boolean allowGaps, int i);
//	public int getNumSelectedCodeLines(boolean allowGaps);
}
