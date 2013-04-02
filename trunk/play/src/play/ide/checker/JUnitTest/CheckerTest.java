package play.ide.checker.JUnitTest;

import org.junit.Test;

import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYTag;
import play.higraph.model.PLAYWholeGraph;
import play.ide.checker.Checker;
import play.ide.checker.symbolTable.Constness;
import tm.backtrack.BTTimeManager;

/**
 * 
 * @author Shiwei Han
 * 
 */
public class CheckerTest {
	
	private void checkAndPrint(PLAYWholeGraph whole){
		
		for(int i=0;i<whole.getTops().size();i++){
		
			System.out.print(whole.getTop(i).getPayload().getPayloadValue());
			System.out.println("\t\t Total nodes: "+(whole.getTop(i).getDescendants().size()+1));
		}	
		
		System.out.println("----------------------------------------------");
		
		Checker checker = new Checker(whole);
		checker.check();
		
		System.out.println("==============================================");
		
	}

	@Test //two vardecls - no error
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
	
	@Test //two vardecls - name duplicate
	public void testCheckClass2() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class2", PLAYTag.CLASS, "Class2"));
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
	
	@Test //vardecl - number literal to string type
	public void testCheckClass3_1() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class3_1", PLAYTag.CLASS, "Class3_1"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace( PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		checkAndPrint(whole);
	}
	
	@Test  //error: vardecl - string literal to number type
	public void testCheckClass3_2() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class3_2", PLAYTag.CLASS, "Class3_2"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace( PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.STRINGLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("abc");
		
		checkAndPrint(whole);
	}
	
	@Test // vardecl with combined types bool + number
	public void testCheckClass4() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class4", PLAYTag.CLASS, "Class4"));
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
	
	@Test // vardecl with combined types number+String+bool
	public void testCheckClass5() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class5", PLAYTag.CLASS, "Class5"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.ALTTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(0, PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(1, PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(2, PLAYTag.BOOLEANTYPE);
		
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		checkAndPrint(whole);
	}

	@Test //Find type by exp (number literal)
	public void testCheckClass6() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class6", PLAYTag.CLASS, "Class6"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
		
		checkAndPrint(whole);
	}

	@Test //Find type by exp (number literal then this var)
	public void testCheckClass7() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class7", PLAYTag.CLASS, "Class7"));
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
	
	@Test	//error: Field z not found
	public void testCheckClass8() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class8", PLAYTag.CLASS, "Class8"));
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
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("z");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass9_1() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class9_1", PLAYTag.CLASS, "Class9_1"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.THISVAR);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("y");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("5");
		
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass9_2() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class9_2", PLAYTag.CLASS, "Class9_2"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.THISVAR);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("y");
		
		//VARDECL y
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("5");
		
		checkAndPrint(whole);
	}
	
	@Test // error: in vardecl neither type nor exp is defined.
	public void testCheckClass10() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class10", PLAYTag.CLASS, "Class10"));
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
	public void testCheckClass11() {//compare with 20
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class11", PLAYTag.CLASS, "Class11"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.METHODTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(0, PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(1, PLAYTag.STRINGTYPE);	
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(1).replace(PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("y");
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
		whole.getTop(0).getChild(0).getChild(1).getChild(1).replace(PLAYTag.STRINGTYPE);
		
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
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("y");
		checkAndPrint(whole);
	}

	@Test
	public void testCheckClass14() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class14", PLAYTag.CLASS, "Class14"));
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
	
	@Test
	public void testCheckClass15() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class15", PLAYTag.CLASS, "Class15"));
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
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("y");

		checkAndPrint(whole);
	}	

	@Test
	public void testCheckClass16() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class16", PLAYTag.CLASS, "Class16"));
		whole.makeRootNode(new PLAYPayload("MyClass", PLAYTag.CLASS, "MyClass"));

		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(0).getChild(0).getPayload().setPayloadValue("MyClass");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NOEXP);

		checkAndPrint(whole);
	
	}

	@Test
	public void testCheckClass17() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class17", PLAYTag.CLASS, "Class17"));
		whole.makeRootNode(new PLAYPayload("MyClass", PLAYTag.CLASS, "MyClass"));

		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(0).getChild(0).getPayload().setPayloadValue("MyClass");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NOEXP);
		
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		//VARDECL z
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("z");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.DOT);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("x");

		whole.getTop(1).insertChild(0, PLAYTag.VARDECL);
		
		whole.getTop(1).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(1).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(1).getChild(0).getChild(1).replace( PLAYTag.NOEXP);

		checkAndPrint(whole);
	
	}
	
	@Test
	public void testCheckClass18() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class18", PLAYTag.CLASS, "Class18"));
		whole.makeRootNode(new PLAYPayload("MyClass", PLAYTag.CLASS, "MyClass"));

		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(0).getChild(0).getPayload().setPayloadValue("MyClass");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NOEXP);
		
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		//VARDECL z
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("z");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.DOT);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("x");

		whole.getTop(1).insertChild(0, PLAYTag.VARDECL);
		
		whole.getTop(1).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(1).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(1).getChild(0).getChild(1).replace( PLAYTag.NOEXP);

		checkAndPrint(whole);
	
	}
	
	@Test
	public void testCheckClass19() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class19", PLAYTag.CLASS, "Class19"));
		whole.makeRootNode(new PLAYPayload("MyClass", PLAYTag.CLASS, "MyClass"));

		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(0).getChild(0).getPayload().setPayloadValue("MyClass");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NOEXP);
		
		
		//VARDECL z
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("z");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.BOOLEANTYPE);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.DOT);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("x");

		//VARDECL c
		whole.getTop(0).insertChild(1, PLAYTag.VARDECL);
		whole.getTop(0).getChild(1).getPayload().setPayloadValue("c");
		whole.getTop(0).getChild(1).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(1).getChild(1).replace( PLAYTag.DOT);
		whole.getTop(0).getChild(1).getChild(1).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(1).getChild(1).insertChild(0, PLAYTag.THISVAR);
		whole.getTop(0).getChild(1).getChild(1).getChild(0).getPayload().setPayloadValue("x");

		whole.getTop(1).insertChild(0, PLAYTag.VARDECL);
		
		whole.getTop(1).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(1).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(1).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(1).getChild(0).getChild(1).getPayload().setPayloadValue("a");
		checkAndPrint(whole);
	
	}
	
	
	@Test
	public void testCheckClass20() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class20", PLAYTag.CLASS, "Class20"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.METHODTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(0, PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(0).getChild(0).insertChild(1, PLAYTag.NUMBERTYPE);	
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.STRINGTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(1).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("y");
		checkAndPrint(whole);
	}	
	
	@Test
	public void testCheckClass21() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class21", PLAYTag.CLASS, "Class21"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.CLASSTYPE);
		whole.getTop(0).getChild(0).getChild(0).getPayload().setPayloadValue("MyClass");
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.NUMBERLITERAL);
		whole.getTop(0).getChild(0).getChild(1).getPayload().setPayloadValue("1");
					
		checkAndPrint(whole);
	
	}
	
	@Test
	public void testCheckClass22() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class22", PLAYTag.CLASS, "Class22"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(0).replace(PLAYTag.NOTYPE);

		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(1).replace(PLAYTag.NOTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.IF);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(1).insertChild(0, PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(1).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(2).insertChild(0, PLAYTag.FALSE);
		checkAndPrint(whole);
	}	
	
	@Test
	public void testCheckClass23_1() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class23_1", PLAYTag.CLASS, "Class23_1"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("z");
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass23_2() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class23_2", PLAYTag.CLASS, "Class23_2"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("z");
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(0).replace(PLAYTag.BOOLEANTYPE);
		checkAndPrint(whole);
	}
	
	@Test
	public void testCheckClass24() {
		
		PLAYWholeGraph whole = new PLAYWholeGraph(new BTTimeManager());
		whole.makeRootNode(new PLAYPayload("Class24", PLAYTag.CLASS, "Class24"));
		whole.getTop(0).insertChild(0, PLAYTag.VARDECL);
		 
		//VARDECL x
		whole.getTop(0).getChild(0).getPayload().setPayloadValue("x");
		whole.getTop(0).getChild(0).getPayload().setConstness(Constness.CON);
		whole.getTop(0).getChild(0).getChild(1).replace( PLAYTag.METHOD);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getPayload().setPayloadValue("y");
		whole.getTop(0).getChild(0).getChild(1).getChild(0).getChild(0).getChild(0).replace(PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(0, PLAYTag.VARDECL);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getPayload().setPayloadValue("z");
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(0).replace(PLAYTag.ALTTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(0).insertChild(0, PLAYTag.NUMBERTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(0).insertChild(0, PLAYTag.BOOLEANTYPE);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(1).replace(PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(0).getChild(1).getPayload().setPayloadValue("y");
		
		whole.getTop(0).getChild(0).getChild(1).getChild(2).insertChild(1, PLAYTag.ASSIGN);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(1).getChild(0).replace(PLAYTag.LOCALVAR);
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(1).getChild(0).getPayload().setPayloadValue("z");
		whole.getTop(0).getChild(0).getChild(1).getChild(2).getChild(1).getChild(1).replace(PLAYTag.TRUE);
		checkAndPrint(whole);
	}


}
