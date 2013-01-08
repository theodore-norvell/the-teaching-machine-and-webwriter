/**
 * 
 */
package tm.backtrack;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * @author theo
 *
 */
public class BackTrackTests  {
	
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
	
	@Test public void testBTVar() {
		BTVar<Integer> x = new BTVar<Integer>(timeMan, 13) ;
		
		assertTrue( ! timeMan.canUndo() ) ;
		assertEquals(13, x.get().intValue() ) ;
		
		timeMan.checkpoint() ;
		
		assertEquals(13, x.get().intValue() ) ;
		assertTrue( timeMan.canUndo() );
		
		x.set( 42 ) ;
		
		assertEquals(42, x.get().intValue() ) ;
		assertTrue( timeMan.canUndo() );
		
		timeMan.undo() ; 
		
		assertTrue( ! timeMan.canUndo() ) ;
		assertEquals(13, x.get().intValue() ) ;
		
		x.set(20) ;
		
		timeMan.checkpoint() ;
		timeMan.checkpoint() ;
		
		x.set(40) ;

		timeMan.checkpoint() ;
		
		timeMan.undo();
		timeMan.undo();
		timeMan.checkpoint() ;

		assertEquals(20, x.get().intValue() ) ;
	}
	
	@Test public void testBTArray() {
		
		BTArray<Integer> ba = new BTArray<Integer>(timeMan, 10) ;
		for( int i = 0 ; i < 10 ; ++i ) ba.put(i, i) ;
		timeMan.checkpoint() ;
		timeMan.checkpoint() ; 
		for( int i = 0 ; i < 10 ; ++i) ba.put(i, i+10) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(i+10, ba.get(i).intValue()) ;
		timeMan.undo() ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(i, ba.get(i).intValue() ) ;

		for( int i = 0 ; i < 10 ; ++i ) ba.put(i, 20+i) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(i+20, ba.get(i).intValue() ) ;
		timeMan.undo() ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals(i, ba.get(i).intValue() ) ;
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
	
	@Test public void testBTStack() {
		BTStack<Double> bs = new BTStack<Double>(timeMan) ;

		for( int i = 0 ; i < 10 ; ++i ) bs.push((double)i) ;
		assertEquals(10, bs.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((double)i, bs.get(i).doubleValue() , 0.001) ;
		timeMan.checkpoint() ; //1
		timeMan.checkpoint() ; //2
		for( int i = 0 ; i < 5 ; ++i) bs.pop() ;
		assertEquals(5, bs.size() ) ;
		for( int i = 0 ; i < 10 ; ++i ) bs.push((double)i) ;
		for( int i = 0 ; i < 5 ; ++i) assertEquals((double)i, bs.get(i).doubleValue() , 0.001) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((double)i, bs.get(i+5).doubleValue(), 0.001) ;
		timeMan.undo() ; //1
		assertEquals(10, bs.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((double)i, bs.get(i).doubleValue() , 0.001) ;
		
		for( int i = 0 ; i < 5 ; ++i ) bs.pop()  ;
		assertEquals(5, bs.size() ) ;
		
		timeMan.checkpoint() ; //2a
		assertEquals(5, bs.size() ) ;
		for( int i = 0 ; i < 10 ; ++i ) bs.push(i+20.0) ;
		assertEquals(15, bs.size() ) ;
		for( int i = 0 ; i < 5 ; ++i ) assertEquals((double)i, bs.get(i).doubleValue() , 0.001) ;
		for( int i = 0 ; i < 10; ++i ) assertEquals((double)i+20, bs.get(i+5).doubleValue() , 0.001) ;

		timeMan.undo() ; // 1
		timeMan.undo() ; // 0 

		assertEquals(10, bs.size() ) ;
		for( int i = 0 ; i < 10 ; ++i) assertEquals((double)i, bs.get(i).doubleValue() , 0.001) ;
	}
	
	@Test public void testBTVector() {
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
		for( int i = 0 ; i < 5 ; ++i ) bv.set(i, (char)(2*i+1)) ;
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
	
	@Test public void testBTByteArray() {			
			BTByteArray ba = new BTByteArray(timeMan, 10) ;
			// Time 0
			for( int i = 0 ; i < 10 ; ++i ) ba.putByte(i, (byte)i) ;
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i, ba.getByte(i) ) ;
			timeMan.checkpoint() ;
			// Time 1
			timeMan.checkpoint() ; 
			// Time 2
			for( int i = 0 ; i < 10 ; ++i) ba.putByte(i, (byte)(i+10)) ;
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i+10, ba.getByte(i)) ;
			timeMan.undo() ;
			// Back to time 1
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i, ba.getByte(i) ) ;

			for( int i = 0 ; i < 10 ; ++i ) ba.putByte(i, (byte)(20+i)) ;
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i+20, ba.getByte(i) ) ;
			timeMan.undo() ;
			// Back to time 0
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i, ba.getByte(i) ) ;
			timeMan.undo() ;
			// Still in time 0
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i, ba.getByte(i) ) ;
			// Change values to i+13
			for( int i = 0 ; i < 10 ; ++i ) ba.putByte(i, (byte)(i+13)) ;
			timeMan.checkpoint();
			// Time 1a
			// Change values to i+42
			for( int i = 0 ; i < 10 ; ++i ) ba.putByte(i, (byte)(i+42)) ;
			timeMan.undo();
			// Time 0 again.
			for( int i = 0 ; i < 10 ; ++i) assertEquals(i+13, ba.getByte(i) ) ;
		}
	
}
