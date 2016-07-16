package tm.gwt.jsInterface;

import com.google.gwt.core.client.js.JsProperty;
import com.google.gwt.core.client.js.JsType;

import tm.portableDisplays.StateInterface;
@JsType
public interface MirrorStateTest extends StateInterface{
	@JsProperty
	public String getExpression();
}
