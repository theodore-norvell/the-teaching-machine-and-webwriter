/*
 * Created on 2009-10-07 by Theodore S. Norvell. 
 */
package higraphTests;

import static org.junit.Assert.* ;

import higraphTests.model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tm.backtrack.BTTimeManager;

/**
 * @author theo
 */
public class TestInsertNode {
    
    TestWholeGraph wg ;
    TestSubgraph mainSG ;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        wg = new TestWholeGraph( new BTTimeManager() ) ;
        mainSG = wg.makeSubGraph() ;
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
    
    @Test public void insertVsSubgraphs0() {
        TestPayload pp = new TestPayload("Parent") ;
        TestNode pn = wg.makeRootNode( pp ) ;
        mainSG.addTop( pn ) ;
        TestPayload cp = new TestPayload("Child") ;
        TestNode cn = wg.makeRootNode( cp ) ;
        mainSG.addTop(cn ) ;
        
        assertTrue( wg.getTops().contains(pn)) ;
        assertTrue( wg.getTops().contains(cn)) ;
        assertTrue( mainSG.getTops().contains( pn ) ) ;
        assertTrue( mainSG.getTops().contains( cn ) ) ;
        assertEquals( 0, pn.getNumberOfChildren() ) ;
        assertEquals( 0, cn.getNumberOfChildren() ) ;
        assertNull( pn.getParent() ) ;
        assertNull( cn.getParent() ) ;
        
        
        assertTrue( pn.canInsertChild( 0, cn ) ) ;
        
        pn.insertChild( 0, cn ) ;
    
        assertTrue( wg.getTops().contains(pn)) ;
        assertTrue( ! wg.getTops().contains(cn)) ;
        assertTrue( mainSG.getTops().contains( pn ) ) ;
        assertTrue( ! mainSG.getTops().contains( cn ) ) ;
        assertEquals( 1, pn.getNumberOfChildren() ) ;
        assertEquals( 0, cn.getNumberOfChildren() ) ;
        assertNull( pn.getParent() ) ;
        assertEquals(pn, cn.getParent() ) ;
        assertEquals( cn, pn.getChild(0)) ;
        
        // The following call should be harmless
        mainSG.removeTop( cn ) ;
    
        assertTrue( wg.getTops().contains(pn)) ;
        assertTrue( ! wg.getTops().contains(cn)) ;
        assertTrue( mainSG.getTops().contains( pn ) ) ;
        assertTrue( ! mainSG.getTops().contains( cn ) ) ;
        assertEquals( 1, pn.getNumberOfChildren() ) ;
        assertEquals( 0, cn.getNumberOfChildren() ) ;
        assertNull( pn.getParent() ) ;
        assertEquals(pn, cn.getParent() ) ;
        assertEquals( cn, pn.getChild(0)) ;
    }
    

}
