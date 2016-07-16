package tm.gwt.jsInterface;

import com.google.gwt.core.client.js.JsProperty;

import tm.portableDisplays.StateInterface;
//@JsType
public interface MirrorState extends StateInterface{
	@JsProperty
	public String getExpression();
}
