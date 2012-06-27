package tm.evaluator;

import tm.clc.parser.CommonParserHelper;
import tm.utilities.ApologyException;
import tm.utilities.RunTimeException;
import junit.framework.*;


public class TestCommandStringInterpreter  extends TestCase {
	private class MockCommandable implements CommandStringInterpreter.CommandsI {
		StringBuffer b = new StringBuffer() ;

		@Override
		public void intoSubCommand() {
			b.append('s') ;
		}

		@Override
		public void intoExpCommand() {
			b.append('e') ;
		}

		@Override
		public void overAllCommand() {
			b.append('o') ;
		}

		@Override
		public void goForwardCommand() {
			b.append('f') ;
		}

		@Override
		public void toBreakPointCommand() {
			b.append('b') ;
		}

		@Override
		public void microStepCommand() {
			b.append('m') ;
		}
		
		String result() { return b.toString() ; }
		
	}
	
	MockCommandable mockup = null ;
	
	public TestCommandStringInterpreter(String name ) {
		super(name) ;
	}
	
	public void setUp() {
		mockup = new MockCommandable() ;
	}
	
	public void tearDown() {
		mockup = new MockCommandable() ;
	}

	public void test0() {
		CommandStringInterpreter csi = new CommandStringInterpreter("", mockup) ;
		csi.interpretGoCommand() ;
		assertEquals("", mockup.result() ) ;
	}

	public void test1() {
		CommandStringInterpreter csi = new CommandStringInterpreter("b", mockup) ;
		csi.interpretGoCommand() ;
		assertEquals("b", mockup.result() ) ;
	}

	public void test2() {
		CommandStringInterpreter csi = new CommandStringInterpreter("s;e;o;f;b;m;s;e;o;f;b;m", mockup) ;
		csi.interpretGoCommand() ;
		assertEquals("seofbmseofbm", mockup.result() ) ;
	}

	public void test3() {
		CommandStringInterpreter csi = new CommandStringInterpreter(" s ; e ;  o ; f ; b ; m ; ", mockup) ;
		csi.interpretGoCommand() ;
		assertEquals("seofbm", mockup.result() ) ;
	}

	public void test4() {
		CommandStringInterpreter csi = new CommandStringInterpreter("3*s", mockup) ;
		csi.interpretGoCommand() ;
		assertEquals("sss", mockup.result() ) ;
	}


	public void test5() {
		CommandStringInterpreter csi = new CommandStringInterpreter("3 * s; 12 * b", mockup) ;
		csi.interpretGoCommand() ;
		assertEquals("sssbbbbbbbbbbbb", mockup.result() ) ;
	}


	public void testError0() {
		CommandStringInterpreter csi = new CommandStringInterpreter("x", mockup) ;
		try {
			csi.interpretGoCommand() ;
			assertTrue(false) ;
		} catch(tm.utilities.RunTimeException e ) {
			
		}
	}


	public void testError1() {
		CommandStringInterpreter csi = new CommandStringInterpreter("3", mockup) ;
		try {
			csi.interpretGoCommand() ;
			assertTrue(false) ;
		} catch(tm.utilities.RunTimeException e ) {
			
		}
	}


	public void testError2() {
		CommandStringInterpreter csi = new CommandStringInterpreter("3 * ", mockup) ;
		try {
			csi.interpretGoCommand() ;
			assertTrue(false) ;
		} catch(tm.utilities.RunTimeException e ) {
			
		}
	}


	public void testError3() {
		CommandStringInterpreter csi = new CommandStringInterpreter("3*bs", mockup) ;
		try {
			csi.interpretGoCommand() ;
			assertTrue(false) ;
		} catch(tm.utilities.RunTimeException e ) {
			
		}
	}


	public void testError4() {
		CommandStringInterpreter csi = new CommandStringInterpreter("5o", mockup) ;
		try {
			csi.interpretGoCommand() ;
			assertTrue(false) ;
		} catch(tm.utilities.RunTimeException e ) {
			
		}
	}
}
