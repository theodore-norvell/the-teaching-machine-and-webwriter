package mun.client;

import com.google.gwt.core.client.js.JsType;

@JsType
public class TestJsType {
	public boolean bool = true;

	  public TestJsType() {
//	    this.bool = bool;
	  }

	  public String aPublicMethod() {
	    return "Hello ";
	  }

	  public static String aStaticMethd() {
	    return "What's the meaning of life?";
	  }
}
