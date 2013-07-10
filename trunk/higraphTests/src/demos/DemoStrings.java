/*
 * Created on 10 July, 2013 by Theodore S. Norvell. 

 */

package demos;

import tm.scripting.ScriptManager;

public class DemoStrings {

	// Colors
	public static final int WHITE = 0xffffff;
	public static final int BLACK = 0x000000;
	public static final int GREY = 0x808080;
	public static final int RED = 0xff0000;
	public static final int GREEN = 0x00ff00;
	public static final int BLUE = 0x2419FA;

	// String markers
	public static final int MARKER_RED = 0xffff;	
	public static final int MARKER_BLUE = 0xfffe;	
	public static final int MARKER_BLACK = 0xfffd;	
	public static final int MARKER_GREEN = 0xfffc;
	public static final int END_MARKER = 0xfff0;

    
    public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph", "Higraph.PDV", "SimpleTree");
        ScriptManager.relay("HigraphManager", "setTitle", "mainView", "Strings demo");
    }
   
    public static void main(String[] str) {
    	setup();
        // Make a mutable string named "a". Initially it is empty.
    	ScriptManager.relay("HigraphManager", "createString", "a" ) ;
    	ScriptManager.relay("HigraphManager", "addToString", "a", "This is string 'a'." ) ;
    	ScriptManager.relay("HigraphManager", "placeString", "a", 100, 50) ;
    	ScriptManager.relay("HigraphManager", "setStringBaseColor", "a", BLUE );
    	ScriptManager.relay("HigraphManager", "markSubString", "a", "'a'", MARKER_RED, 0) ;
    	ScriptManager.relay("HigraphManager", "removeString", "a" ) ;
    }
}