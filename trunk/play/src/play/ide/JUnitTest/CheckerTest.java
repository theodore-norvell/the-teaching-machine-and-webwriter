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
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("hello");
		
		System.out.println("Test Sample: Class1    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
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
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		System.out.println("Test Sample: Class2    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
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
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		System.out.println("Test Sample: Class3    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
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
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(1).getChild(0).getPayload().setPayloadValue("My2ndClass");
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("hello");
						
		
		System.out.println("Test Sample: Class4    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
	
	}
	
	@Test
	public void testCheckClass5() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class5", PLAYTag.CLASS, "Class5"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		System.out.println("Test Sample: Class5    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
	}
	
	@Test
	public void testCheckClass6() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class6", PLAYTag.CLASS, "Class6"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		//VARDECL X
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("hello");
		
		System.out.println("Test Sample: Class6    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
	}
	
	@Test
	public void testCheckClass7() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class7", PLAYTag.CLASS, "Class7"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("x");
		
		System.out.println("Test Sample: Class7    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
	}
	
	@Test
	public void testCheckClass8() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class8", PLAYTag.CLASS, "Class8"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("z");
		
		System.out.println("Test Sample: Class8    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
	}
	
	@Test
	public void testCheckClass9() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class9", PLAYTag.CLASS, "Class9"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(0).getChild(1).insertChild(0, PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("1");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("x");
		
		System.out.println("Test Sample: Class9    "+"Total nodes:"+whole.getNodes().size());

		System.out.println("------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		System.out.println("====================================");
	}	
	
}
