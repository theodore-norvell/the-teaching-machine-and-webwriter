package play.ide.JUnitTest;

import static org.junit.Assert.*;
import org.junit.Test;
import play.higraph.model.*;
import play.ide.checker.Checker;
import play.ide.checker.symbolTable.Constness;
import tm.backtrack.BTTimeManager;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class CheckerTest {

	@Test
	public void testCheckClass1() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class1", PLAYTag.CLASS, "Class1"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("hello");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass2() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class2", PLAYTag.CLASS, "Class2"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.ALTTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(0, PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(1, PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass3() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class3", PLAYTag.CLASS, "Class3"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.ALTTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(0, PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(1, PLAYTag.BOOLEANTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace(PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass4() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class4", PLAYTag.CLASS, "Class4"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(0).getChild(0).getPayload().setPayloadValue("MyClass");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(1).getChild(0).getPayload().setPayloadValue("My2ndClass");
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("hello");
						
		
		checkAndPrint(whole);
	
	}
	
	@Test
	public void testCheckClass5() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class5", PLAYTag.CLASS, "Class5"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass6() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class6", PLAYTag.CLASS, "Class6"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		//VARDECL X
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("hello");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass7() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class7", PLAYTag.CLASS, "Class7"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("x");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass8() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class8", PLAYTag.CLASS, "Class8"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");

		//VARDECL y
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NUMBERTYPE);
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass9() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class9", PLAYTag.CLASS, "Class9"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("x");
		
		checkAndPrint(whole);
	}	
	
	@Test
	public void testCheckClass10() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class10", PLAYTag.CLASS, "Class10"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(1).replace(PLAYTag.NUMBERTYPE);
		
		checkAndPrint(whole);
	}	
	
	@Test
	public void testCheckClass11() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class11", PLAYTag.CLASS, "Class11"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(1).replace(PLAYTag.STRINGTYPE);
		
		checkAndPrint(whole);
	}	
	
	@Test
	public void testCheckClass12() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class12", PLAYTag.CLASS, "Class12"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("y");
		checkAndPrint(whole);
	}
	
	
	
	@Test
	public void testCheckClass13() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class13", PLAYTag.CLASS, "Class13"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("y");
		checkAndPrint(whole);
	}
	
	private void checkAndPrint(PLAYWholeGraph whole){
		System.out.println("Test Sample: "+whole.getTop(0).getPayload().getPayloadValue()+"    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("--------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("======================================");
	}
	
	
}
