package tm.gwt.jsInterface;

import tm.interfaces.StateInterface ;

import com.google.gwt.core.client.js.JsProperty;
import com.google.gwt.core.client.js.JsType;

@JsType
public interface MirrorStateTest extends StateInterface{
	@JsProperty
	public String getExpression();
}
