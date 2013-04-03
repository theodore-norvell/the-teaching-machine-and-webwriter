package tm.backtrack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.*;

import tm.utilities.AssertException;

public class BackTrackTests2 {

	
	BTTimeManager timeMan ;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before public void setUp() throws Exception {
		timeMan = new BTTimeManager() ;
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After public void tearDown() throws Exception {
	}
	
	@Test public void testBTVar0() {
		BTVar<Integer> x = new BTVar<Integer>(timeMan, 13) ;
		BTVar<Integer> y = new BTVar<Integer>(timeMan, 14) ;
		
		assertTrue( ! timeMan.canUndo() ) ;
		assertEquals(13, x.get().intValue() ) ;
		assertEquals(14, y.get().intValue() ) ;
		
		timeMan.checkpoint() ;
		
		assertEquals(13, x.get().intValue() ) ;
		assertEquals(14, y.get().intValue() ) ;
		
		assertTrue( timeMan.canUndo() );
		
		x.set( 42 ) ;
		y.set( 43 ) ;
		timeMan.checkpoint() ;
		timeMan.undo() ;
		
		assertEquals(42, x.get().intValue() ) ;
		assertEquals(43, y.get().intValue() ) ;
		
		assertTrue( timeMan.canUndo() );
		
		timeMan.undo() ; 
		
		assertTrue( ! timeMan.canUndo() ) ;
		assertEquals(13, x.get().intValue() ) ;
		assertEquals(14, y.get().intValue() ) ;
		
		x.set(20) ;
		y.set(21) ;
		
		timeMan.checkpoint() ;
		timeMan.checkpoint() ;
		
		x.set(40) ;
		y.set(41) ;

		timeMan.checkpoint() ;
		
		timeMan.undo();
		timeMan.undo();
		timeMan.checkpoint() ;

		assertEquals(20, x.get().intValue() ) ;
		assertEquals(21, y.get().intValue() ) ;
	}
		
	@Test public void testBTVarRedo0() {
		// Time 0
		BTVar<Integer> x = new BTVar<Integer>(timeMan, 3) ;
		BTVar<Integer> y = new BTVar<Integer>(timeMan, 4) ;
		
		assertTrue( ! timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		
		timeMan.checkpoint() ;
		// Time 1
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		x.set(13) ;
		y.set(14) ;
		
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		x.set(15) ;
		y.set( 16 ) ;
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		
		timeMan.checkpoint();

		// Time 2
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		timeMan.checkpoint();


		// Time 3
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		x.set( 30) ;
		y.set( 31 ) ;
		
		assertEquals(30, x.get().intValue() ) ;
		assertEquals(31, y.get().intValue() ) ;

		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		timeMan.undo() ;
		
		// Time 2
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;

		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		timeMan.undo() ;
		
		// Time 1
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;

		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		
		timeMan.undo() ;
		// Time 0
		assertTrue( !timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		assertEquals(3, x.get().intValue() ) ;
		assertEquals(4, y.get().intValue() ) ;
		
		timeMan.redo() ;
		// Time 1
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;

		timeMan.redo() ;
		
		// Time 2
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		
		timeMan.redo() ;
		// Time 3
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		assertEquals(30, x.get().intValue() ) ;
		assertEquals(31, y.get().intValue() ) ;
		
		timeMan.undo();
		// Time 2
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		
		// Now change at time 2
		x.set( 25 ) ;
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		assertEquals(25, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		
		timeMan.undo();
		// Time 1
		assertEquals(15, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		
		timeMan.redo();
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		assertEquals(25, x.get().intValue() ) ;
		assertEquals(16, y.get().intValue() ) ;
	}
	
	@Test public void testBTVarRedo1() {
		// 0 
		BTVar<Integer> v0 = new BTVar<Integer>(timeMan, 3 ) ;
		assertTrue( v0.alive ) ;
		
		timeMan.checkpoint() ;
		//1
		BTVar<Integer> v1 = new BTVar<Integer>(timeMan, 15 ) ;
		v0.set( 4 ) ;

		assertTrue( v0.alive() ) ;
		assertTrue( v1.alive() ) ;
		assertEquals( 4, v0.get().intValue() ) ;
 		assertEquals( 15, v1.get().intValue() ) ;
 		
 		timeMan.undo() ;
 		// 0

 		assertTrue( v0.alive() ) ;
		assertTrue( ! v1.alive() ) ;
		assertEquals( 3, v0.get().intValue() ) ;
		try {
			v1.get() ;
			assertTrue( false ) ;
		} catch( AssertException e ) {} ;
 		
 		timeMan.redo();
 		// 1

		assertTrue( v0.alive() ) ;
		assertTrue( v1.alive() ) ;
		assertEquals( 4, v0.get().intValue() ) ;
 		assertEquals( 15, v1.get().intValue() ) ;
 		
	}
	
	@Test public void testBTVector0() {
		BTVector<Character> bv = new BTVector<Character>(timeMan) ;

		for( int i = 0 ; i < 10 ; ++i ) bv.add((char)i) ;
		// [0,1,2,3,4,5,6,7,8,9]
		assertEquals(10, bv.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((char)i, bv.get(i).charValue() ) ;
		timeMan.checkpoint() ; //1
		timeMan.checkpoint() ; //2
		for( int i = 0 ; i < 5 ; ++i) bv.removeElementAt(9 - i*2) ;
		
		// [0,2,4,6,8]
		assertEquals(5, bv.size() ) ;
		for( int i = 0 ; i < 5 ; ++i) assertEquals((char)(i*2), bv.get(i).charValue() ) ;
		
		for( int i = 0 ; i < 5 ; ++i ) bv.set(i, (char)(2*i+1)) ;
		
		// [1,3,5,7,9]
		assertEquals(5, bv.size() ) ;
		for( int i = 0 ; i < 5 ; ++i) assertEquals((char)(2*i+1), bv.get(i).charValue() ) ;

		timeMan.undo() ; //1

		assertEquals(10, bv.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((char)i, bv.get(i).charValue() ) ;
		
		timeMan.redo() ; // 2

		assertEquals(5, bv.size() ) ;
		// [1,3,5,7,9]
		for( int i = 0 ; i < 5 ; ++i) assertEquals((char)(2*i+1), bv.get(i).charValue() ) ;

		timeMan.undo() ; //1

		assertEquals(10, bv.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((char)i, bv.get(i).charValue() ) ;
		
		
		bv.insertElementAt('a', 5) ;
		
		assertEquals(11, bv.size() ) ;
		for( int i = 0 ; i < 5 ; ++i) assertEquals((char)i, bv.get(i).charValue() ) ;
		assertEquals('a', bv.get(5).charValue() ) ;
		for( int i = 6 ; i < 11 ; ++i) assertEquals((char)(i-1), bv.get(i).charValue() ) ;
		
		timeMan.checkpoint() ; //2a
		
		assertEquals(11, bv.size() ) ;
		for( int i = 0 ; i < 5 ; ++i) assertEquals((char)i, bv.get(i).charValue() ) ;
		assertEquals('a', bv.get(5).charValue() ) ;
		for( int i = 6 ; i < 11 ; ++i) assertEquals((char)(i-1), bv.get(i).charValue() ) ;

		timeMan.undo() ; // 1
		timeMan.undo() ; // 0 

		assertEquals(10, bv.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((char)i, bv.get(i).charValue() ) ;
	}
	
	@Test public void testBTVector1() {
		BTVector<Character> bv = new BTVector<Character>(timeMan) ;
		assertEquals( 0, bv.size() ) ;
		bv.add( 'a' ) ;
		bv.add('b') ;
		
		timeMan.checkpoint() ;
		
		bv.remove('a') ;
		assertEquals( 1, bv.size() ) ;
		assertEquals( 'b', (char) bv.get(0) ) ;
		try{
			char x = (char) bv.get(1) ;
			assertTrue( false ) ; }
		catch( tm.utilities.AssertException ex ) { }
		
		bv.add( 'c' ) ;
		bv.add('d' ) ;
		assertEquals( 3, bv.size() ) ;
		assertEquals( 'b', (char) bv.get(0) ) ;
		assertEquals( 'c', (char) bv.get(1) ) ;
		assertEquals( 'd', (char) bv.get(2) ) ;
		
		timeMan.undo(); 

		assertEquals( 2, bv.size() ) ;
		assertEquals( 'a', (char) bv.get(0) ) ;
		assertEquals( 'b', (char) bv.get(1) ) ;
		try{
			char x = (char) bv.get(2) ;
			assertTrue( false ) ; }
		catch( tm.utilities.AssertException ex ) { }
		
		timeMan.redo(); 
		assertEquals( 3, bv.size() ) ;
		assertEquals( 'b', (char) bv.get(0) ) ;
		assertEquals( 'c', (char) bv.get(1) ) ;
		assertEquals( 'd', (char) bv.get(2) ) ;
	}

	
	@Test public void testBTVector2() {
		BTVector<String> bv = new BTVector<String>(timeMan);
		assertEquals( 0, bv.size() ) ; 
		// Time 0
		timeMan.checkpoint() ;
		
		// Time 1
		bv.add("b") ;
		
		timeMan.undo( ) ;
		
		// Time 0
		
		assertEquals( 0, bv.size() ) ;
		
		bv.add( "c" ) ;
		
		
		
	}
	


	
	@Test public void testBTVector3() {
		BTVector<String> bv = new BTVector<String>(timeMan);
		assertEquals( 0, bv.size() ) ; 
		// Time 0
		timeMan.checkpoint() ;
		
		// Time 1
		bv.add("b") ;
		bv.add("c") ;
		bv.remove("b") ;
		bv.add("d") ;
		bv.remove("d") ;
		assertEquals("c", bv.get(0)) ;
		
		timeMan.undo( ) ;
		
		// Time 0
		
		assertEquals( 0, bv.size() ) ;
		
		bv.add( "z" ) ;
		
		
		
	}
	@Test public void testBTFunction() {
		BTFunction<String, String> bf = new BTFunction<String, String>(timeMan) ;

		for( int i = 0 ; i < 10 ; ++i ) bf.map(""+i,""+i) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(""+i, bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		timeMan.checkpoint() ; //1
		timeMan.checkpoint() ; //2
		for( int i = 0 ; i < 20 ; ++i) bf.map(""+i, i+10+"") ;
		for( int i = 0 ; i < 20 ; ++i) assertEquals(i+10+"", bf.at(""+i) ) ;
		timeMan.undo() ; //1
		for( int i = 0 ; i < 10 ; ++i ) bf.map(""+i,""+i) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(""+i, bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		timeMan.checkpoint() ; //2a
		for( int i = 0 ; i < 10 ; ++i ) bf.map(""+i,""+i) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(""+i, bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		timeMan.undo() ; // 1
		timeMan.undo() ; // 0 
		for( int i = 0 ; i < 10 ; ++i ) bf.map(""+i,""+i) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(""+i, bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
	}
	
	@Test public void testBTFunctionRedo() {
		// 0
		BTFunction<String, String> bf = new BTFunction<String, String>(timeMan) ;
		// [null, ...]
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		
		timeMan.checkpoint() ;
		// 1
		for( int i = 0 ; i < 10 ; ++i ) bf.map(""+i,""+i) ;
		// [0,1,2,...,9, null, null, ... ]
		for( int i = 0 ; i < 10 ; ++i) assertEquals(""+i, bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		
		timeMan.undo() ;
		// 0
		// [null, ...]
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		
		timeMan.redo();
		// 1
		// [0,1,2,...,9, null, null, ... ]
		for( int i = 0 ; i < 10 ; ++i) assertEquals(""+i, bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		
		timeMan.undo() ;
		// 0
		// [null, ...]
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(null, bf.at(""+i) ) ;
		
		// Make some changes at time 0
		for( int i = 10 ; i < 20 ; ++i) bf.map(""+i, i+10+"") ;
		
		// [null, null, ..., null, 20, 21, ..., 29]
		for( int i = 0 ; i < 10 ; ++i) assertEquals(null,   bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(i+10+"", bf.at(""+i) ) ;
		
		assertTrue( ! timeMan.canRedo() ) ;
		
		timeMan.checkpoint() ; //1a 
		// [null, null, ..., null, 20, 21, ..., 29]
		for( int i = 0 ; i < 10 ; ++i) assertEquals(null,   bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(i+10+"", bf.at(""+i) ) ;
		
		for( int i = 0 ; i < 20 ; ++i) bf.map(""+i, i+20+"") ;
		// [20, 21, ..., 39]
		for( int i = 0 ; i < 20 ; ++i) assertEquals(i+20+"", bf.at(""+i) ) ;
		
		timeMan.undo() ; // 0

		// [null, null, ..., null, 20, 21, ..., 29]
		for( int i = 0 ; i < 10 ; ++i) assertEquals(null,   bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(i+10+"", bf.at(""+i) ) ;
		
		timeMan.redo(); // 1a
		// [20, 21, ..., 39]
		for( int i = 0 ; i < 20 ; ++i) assertEquals(i+20+"", bf.at(""+i) ) ;
		
		timeMan.undo() ; // 0

		// [null, null, ..., null, 20, 21, ..., 29]
		for( int i = 0 ; i < 10 ; ++i) assertEquals(null,   bf.at(""+i) ) ;
		for( int i = 10 ; i < 20 ; ++i ) assertEquals(i+10+"", bf.at(""+i) ) ;
	}
	
	@Test public void testBTByteArray() {			
			BTByteArray ba = new BTByteArray(timeMan, 1000000) ;
			// Time 0
			for( int i = 0 ; i < 1000000 ; ++i ) ba.putByte(i, (byte)i) ;
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)i, ba.getByte(i) ) ;
			timeMan.checkpoint() ;
			// Time 1
			timeMan.checkpoint() ; 
			// Time 2
			for( int i = 0 ; i < 1000000 ; ++i) ba.putByte(i, (byte)(i+10)) ;
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)(i+10), ba.getByte(i)) ;
			timeMan.undo() ;
			// Back to time 1
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)i, ba.getByte(i) ) ;

			for( int i = 0 ; i < 1000000 ; ++i ) ba.putByte(i, (byte)(20+i)) ;
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)(i+20), ba.getByte(i) ) ;
			timeMan.undo() ;
			// Back to time 0
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)i, ba.getByte(i) ) ;
			timeMan.undo() ;
			// Still in time 0
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)i, ba.getByte(i) ) ;
			// Change values to i+13
			for( int i = 0 ; i < 1000000 ; ++i ) ba.putByte(i, (byte)(i+13)) ;
			timeMan.checkpoint();
			// Time 1a
			// Change values to i+42
			for( int i = 0 ; i < 1000000 ; ++i ) ba.putByte(i, (byte)(i+42)) ;
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)(i+42), ba.getByte(i) ) ;
			timeMan.undo();
			// Time 0 again.
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)(i+13), ba.getByte(i) ) ;
			
			timeMan.redo();
			for( int i = 0 ; i < 1000000 ; ++i) assertEquals((byte)(i+42), ba.getByte(i) ) ;
		}
	
	@Test public void testBTByteArrayRedo0() {
		final int bytesPerLine = 64 ;
		final int hashSize = 1024 ;
		// Time 0
		BTByteArray x = new BTByteArray(timeMan, 100000) ;
		BTByteArray y = new BTByteArray(timeMan, 100000) ;
		
		assertTrue( ! timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		
		timeMan.checkpoint() ;
		// Time 1
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		x.putByte(0, (byte)11) ;
		x.putByte(63, (byte)12) ;
		x.putByte(bytesPerLine, (byte)13) ;
		x.putByte(bytesPerLine*hashSize, (byte)14) ;
		y.putByte(0, (byte)15) ;
		
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		x.putByte(0, (byte)16) ;
		x.putByte(63, (byte)17) ;
		x.putByte(bytesPerLine, (byte)18) ;
		x.putByte(bytesPerLine*hashSize, (byte)19) ;
		y.putByte(0, (byte)10) ;
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;
		
		timeMan.checkpoint();

		// Time 2
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		timeMan.checkpoint();


		// Time 3
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;

		x.putByte(0, (byte)31) ;
		x.putByte(63, (byte)32) ;
		x.putByte(bytesPerLine, (byte)33) ;
		x.putByte(bytesPerLine*hashSize, (byte)34) ;
		y.putByte(0, (byte)35) ;
		
		assertEquals(31, x.getByte(0) ) ;
		assertEquals(32, x.getByte(63) ) ;
		assertEquals(33, x.getByte(bytesPerLine) ) ;
		assertEquals(34, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(35, y.getByte(0) ) ;

		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		timeMan.undo() ;
		
		// Time 2
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;

		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		timeMan.undo() ;
		
		// Time 1
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;

		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		
		timeMan.undo() ;
		// Time 0
		assertTrue( !timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		
		assertEquals(0, x.getByte(0) ) ;
		assertEquals(0, x.getByte(63) ) ;
		assertEquals(0, x.getByte(bytesPerLine) ) ;
		assertEquals(0, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(0, y.getByte(0) ) ;
		
		timeMan.redo() ;
		// Time 1
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;

		timeMan.redo() ;
		
		// Time 2
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;
		
		timeMan.redo() ;
		// Time 3
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		
		assertEquals(31, x.getByte(0) ) ;
		assertEquals(32, x.getByte(63) ) ;
		assertEquals(33, x.getByte(bytesPerLine) ) ;
		assertEquals(34, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(35, y.getByte(0) ) ;
		
		timeMan.undo();
		// Time 2
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;
		
		// Now change at time 2

		x.putByte(0, (byte)21) ;
		x.putByte(bytesPerLine*hashSize, (byte)24) ;
		y.putByte(0, (byte)20) ;
		
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		
		assertEquals(21, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(24, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(20, y.getByte(0) ) ;
		
		timeMan.undo();
		// Time 1
		
		assertEquals(16, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(19, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(10, y.getByte(0) ) ;
		
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( timeMan.canRedo() ) ;
		
		timeMan.redo();
		// Time 2
		assertTrue( timeMan.canUndo() ) ;
		assertTrue( ! timeMan.canRedo() ) ;
		
		assertEquals(21, x.getByte(0) ) ;
		assertEquals(17, x.getByte(63) ) ;
		assertEquals(18, x.getByte(bytesPerLine) ) ;
		assertEquals(24, x.getByte(bytesPerLine*hashSize) ) ;
		assertEquals(20, y.getByte(0) ) ;
	}
}
