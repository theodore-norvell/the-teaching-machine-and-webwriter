package tm.backtrack;

import static org.junit.Assert.* ;

import org.junit.Test ;

public class BackTrackTests3 {

    // This test is based on a test I wrote for PLAAY.
    // I'm putting it here partly to understand the interfaces
    // of the two modules (TM's and PLAAY's) and if there are
    // serious differences.
    @Test
    public void test0() {
        BTTimeManager manager = new BTTimeManager() ;
        assertTrue( ! manager.canUndo() ) ;
        assertTrue( ! manager.canRedo() ) ;
        
        BTVar<Integer> x = new BTVar<Integer>(manager, 0) ;
        assertTrue(  x.get() == 0 );
        x.set( 1 ) ;
        assertTrue( x.get() == 1 ) ;
        
        assertTrue( ! manager.canUndo() ) ;
        assertTrue( ! manager.canRedo() ) ;
        
        // Call this state A: {x -> 1} no parent
        manager.checkpoint();
        
        //TEMP assertTrue( ! manager.canUndo() ) ;
        assertTrue( ! manager.canRedo() ) ;        
        
        assertTrue(  x.get() == 1 ) ;
        x.set( 2 ) ;
        assertTrue( x.get() == 2 ) ;
        
        assertTrue( manager.canUndo() ) ;
        assertTrue( ! manager.canRedo() ) ;
        
        manager.undo() ; // This should take us back to state A
        // It also implicitly checkpoints.
        // So call this state B: {x -> 2} parent is A
        
        // Check that we are in state A.
        assertTrue( ! manager.canUndo() ) ;
        assertTrue( x.get() == 1 ) ;

        // Forward to state B.
        assertTrue( manager.canRedo() );
        manager.redo() ;
        assertTrue( x.get() == 2 ) ;
        assertTrue( ! manager.canRedo() ) ;

        BTVar<String> y = new BTVar<String>(manager, "c") ;
        
        // We've created a new variable that doesn't exist in states A and B
        assertTrue( y.get() == "c" ) ;
        manager.checkpoint(); /*Added! Why is this needed?*/
        manager.undo() ;
        // Another implicit checkpoint, call this state C: { x -> 2, y -> "c" } parent state is B

        // And we should now be back to state B
        
        // Check that we are in state B
        assertEquals(2, x.get().intValue());
        boolean ok = true ;
        try {
            y.get() ;
            ok = false ;
        } catch( Throwable ex ) { }
        assertTrue( ok )  ;
        try {
            y.set( "d" ) ;
            ok = false ;
        } catch( Throwable ex ) {}
        assertTrue( ok )  ;
    
        manager.redo() ; // Back to state C.
        assertEquals( "c", y.get() ) ;
        assertEquals( 2, (int)x.get() ) ;
        
        manager.undo() ; // Back to state B
        // But we can go forward to C if we like
        assertTrue( manager.canRedo() );
    }

}
