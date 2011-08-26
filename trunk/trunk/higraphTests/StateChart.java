/* Created on 11 Aug, 2011 by Theodore S. Norvell. */

import tm.scripting.ScriptManager;
import tm.scripting.PDV; 

class Node{ 
    String n ;
    Node( String n ) { this.n = n ; }
}

public class StateChart {
    
	public static void setup(){
        ScriptManager.relay("HigraphManager", "makeView", "mainView", "wholeGraph","Higraph.PDV", "PlacedNode");
        ScriptManager.relay("HigraphManager", "setDefaultNodeValueShow", false, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeNameShow", false, PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setDefaultNodeFillColor", PDV.WHITE);
        ScriptManager.relay("HigraphManager", "setDefaultNodeShape", PDV.ELLIPSE);
        ScriptManager.relay("HigraphManager", "setDefaultEdgeColor", PDV.BLACK);		
        ScriptManager.relay("HigraphManager", "setDefaultBranchColor", PDV.TRANSPARENT);		
	}
	public static Node makeNode(String i, int x, int y){
		Node n = new Node(i);
        ScriptManager.relay("HigraphManager", "makeNode", n, true );
        ScriptManager.relay("HigraphManager", "placeNode", n, true, x, y );
        ScriptManager.relay("HigraphManager", "createNodeExtraLabel", n, true, "desig", PDV.CENTER);
        ScriptManager.relay("HigraphManager", "setNodeExtraLabel", n, true, "desig", i);
        return n;
	}
    
    public static void main(String [] args) { 
        setup();
        
        Node a = makeNode("a", 0, 0) ;
        Node b = makeNode("b", 200, 0) ;
        
        Node aa = makeNode("aa", 60, 40) ;
        ScriptManager.relay("HigraphManager", "addChild", a, true, aa, true ) ;
        Node ab = makeNode("ab", 30, 100) ;
        ScriptManager.relay("HigraphManager", "addChild", a, true, ab, true ) ;
        
        Node ba = makeNode("ba", 60, 40) ;
        ScriptManager.relay("HigraphManager", "addChild", b, true, ba, true ) ;
        Node bb = makeNode("bb", 30, 100) ;
        ScriptManager.relay("HigraphManager", "addChild", b, true, bb, true ) ;
        
        
        ScriptManager.relay("HigraphManager", "makeEdge", a, true, b, true ) ;
        ScriptManager.relay("HigraphManager", "makeEdge", aa, true, ab, true ) ;
        ScriptManager.relay("HigraphManager", "makeEdge", ba, true, bb, true ) ;
        
        // The following edges might cause some interesting problems
        ScriptManager.relay("HigraphManager", "makeEdge", a, true, aa, true ) ;
        
        ScriptManager.relay("HigraphManager", "makeEdge", bb, true, aa, true ) ;
        
    }
}
