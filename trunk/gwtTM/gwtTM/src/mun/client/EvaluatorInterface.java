package mun.client;

import com.google.gwt.core.client.js.JsProperty;

//@JsType
interface EvaluatorInterface {
	@JsProperty
	String getExpression();
}