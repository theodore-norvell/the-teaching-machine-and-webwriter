/*
 * Created on Dec 12, 2010 by Theodore S. Norvell. 
 */
package higraphTests;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import higraphTests.model.*;
import tm.backtrack.BTTimeManager;
import junit.framework.TestCase;

public class TestGovernedBy extends TestCase {
       
    //                           ...loop
    //                      +--x00 ...
    //               +--x0--|        .
    //               |      +--X01 <..
    //    loop... x -|
    //            .  |      +--x10 ..to y1
    //       to x11  +--x1--|
    //        & y11     .   +--x11...to y11 & y
    //                  to y10&x11
    //
    //                            ...loop
    //                      +--y00 < .
    //               +--y0--|        .
    //               |      +--y01 ...
    //   loop.... y -|
    //               |      +--y10
    //               +--y1--|
    //                      +--y11 ..to y, y1, & x11
    //
    
    BTTimeManager tm = new BTTimeManager() ;
    
    TestWholeGraph wg = new TestWholeGraph(tm) ;
    TestNode x = wg.makeRootNode( new TestPayload("x") ) ;
    TestNode x0 = wg.makeRootNode( new TestPayload("x0") ) ;
    TestNode x00 = wg.makeRootNode( new TestPayload("x00") ) ;
    TestNode x01 = wg.makeRootNode( new TestPayload("x01") ) ;
    TestNode x1 = wg.makeRootNode( new TestPayload("x1") ) ;
    TestNode x10 = wg.makeRootNode( new TestPayload("x10") ) ;
    TestNode x11 = wg.makeRootNode( new TestPayload("x11") ) ;
    TestNode y = wg.makeRootNode( new TestPayload("y") ) ;
    TestNode y0 = wg.makeRootNode( new TestPayload("y0") ) ;
    TestNode y00 = wg.makeRootNode( new TestPayload("y00") ) ;
    TestNode y01 = wg.makeRootNode( new TestPayload("y01") ) ;
    TestNode y1 = wg.makeRootNode( new TestPayload("y1") ) ;
    TestNode y10 = wg.makeRootNode( new TestPayload("y10") ) ;
    TestNode y11 = wg.makeRootNode( new TestPayload("y11") ) ;
    
    TestEdge xx = wg.makeEdge(x, x, new TestEdgeLabel("xx") ) ;
    TestEdge xy11 = wg.makeEdge(x, y11, new TestEdgeLabel("xy11") ) ;
    TestEdge xx11 = wg.makeEdge(x, x11, new TestEdgeLabel("xx11") ) ;
    TestEdge x1x11 = wg.makeEdge(x1, x11, new TestEdgeLabel("x1x11") ) ;
    TestEdge x1y10 = wg.makeEdge(x1, y10, new TestEdgeLabel("x1y10") ) ;
    TestEdge x00x00 = wg.makeEdge(x00, x00, new TestEdgeLabel("x00x00") ) ;
    TestEdge x00x01 = wg.makeEdge(x00, x01, new TestEdgeLabel("x00x01") ) ;
    TestEdge x11y11 = wg.makeEdge(x11, y11, new TestEdgeLabel("x11y11") ) ;
    
    TestEdge yy = wg.makeEdge(y, y, new TestEdgeLabel( "yy" ) ) ;
    TestEdge x11y = wg.makeEdge(x11, y, new TestEdgeLabel("x11y") ) ;
    TestEdge y11y = wg.makeEdge(y11, y, new TestEdgeLabel("y11y") ) ;
    TestEdge y11y1 = wg.makeEdge(y11, y1, new TestEdgeLabel("y11y1") ) ;
    TestEdge x10y1 = wg.makeEdge(x10, y1, new TestEdgeLabel("x10y1") ) ;
    TestEdge y00y00 = wg.makeEdge(y00, y00, new TestEdgeLabel("y00y00") ) ;
    TestEdge y01y00 = wg.makeEdge(y01, y00, new TestEdgeLabel("y01y00") ) ;
    TestEdge y11x11 = wg.makeEdge(y11, x11, new TestEdgeLabel("y11x11") ) ;
       
    TestSubgraph sg = wg.makeSubGraph() ;
    
    {
        // Build the tree structure.  See (above)
        x.insertChild(0, x0) ;
        x.insertChild(1, x1) ;
        x0.insertChild(0, x00 ) ;
        x0.insertChild(1, x01 ) ;
        x1.insertChild(0, x10) ;
        x1.insertChild(1, x11 ) ;
        y.insertChild(0, y0) ;
        y.insertChild(1, y1) ;
        y0.insertChild(0, y00 ) ;
        y0.insertChild(1, y01 ) ;
        y1.insertChild(0, y10) ;
        y1.insertChild(1, y11 ) ;      
        
 
        // The subgraph structure
        sg.addTop(x1) ;
        sg.addTop(y1) ;
    }
    
    public void test_wg() {
        Collection<TestEdge> s = wg.getGovernedEdges() ;
        assertEquals(8, s.size()) ;
        Set<TestEdge> expected = new HashSet<TestEdge>() ;
        expected.add(xx) ;
        expected.add(xy11) ;
        expected.add(x1y10) ;
        expected.add(x11y11) ;
        expected.add(y11x11) ;
        expected.add(x10y1) ;
        expected.add(x11y) ;
        expected.add(yy) ;
        for( TestEdge e : expected ) {
            assertTrue( s.contains(e) ) ; }
    }
    
    public void test_x() {
        Collection<TestEdge> s = x.getGovernedEdges() ;
        assertEquals(1, s.size()) ;
        
        assertTrue( s.contains(xx11) ) ;
    }
    
    public void test_y() {
        Collection<TestEdge> s = y.getGovernedEdges() ;
        assertEquals(1, s.size()) ;
        
        assertTrue( s.contains(y11y) ) ;
    }
    
    public void test_x0() {
        Collection<TestEdge> s = x0.getGovernedEdges() ;
        assertEquals(2, s.size()) ;
        
        Set<TestEdge> expected = new HashSet<TestEdge>() ;
        expected.add(x00x01) ;
        expected.add(x00x00) ;
        for( TestEdge e : expected ) {
            assertTrue( s.contains(e) ) ; }
    }
    
    public void test_y0() {
        Collection<TestEdge> s = y0.getGovernedEdges() ;
        assertEquals(2, s.size()) ;
        
        Set<TestEdge> expected = new HashSet<TestEdge>() ;
        expected.add(y00y00) ;
        expected.add(y01y00) ;
        for( TestEdge e : expected ) {
            assertTrue( s.contains(e) ) ; }
    }
    
    public void test_x1() {
        Collection<TestEdge> s = x1.getGovernedEdges() ;
        assertEquals(1, s.size()) ;
        
        assertTrue( s.contains(x1x11) ) ;
    }
    
    public void test_y1() {
        Collection<TestEdge> s = y1.getGovernedEdges() ;
        assertEquals(1, s.size()) ;
        
        assertTrue( s.contains(y11y1) ) ;
    }
    
    public void test_leaves() {
        assertEquals(0, x00.getGovernedEdges().size()) ;
        assertEquals(0, x01.getGovernedEdges().size()) ;
        assertEquals(0, x10.getGovernedEdges().size()) ;
        assertEquals(0, x11.getGovernedEdges().size()) ;
        assertEquals(0, y00.getGovernedEdges().size()) ;
        assertEquals(0, y01.getGovernedEdges().size()) ;
        assertEquals(0, y10.getGovernedEdges().size()) ;
        assertEquals(0, y11.getGovernedEdges().size()) ;
    }
    
    public void test_sg() {
        Collection<TestEdge> s = sg.getGovernedEdges() ;
        assertEquals(4, s.size()) ;
        
        Set<TestEdge> expected = new HashSet<TestEdge>() ;
        expected.add(x1y10) ;
        expected.add(x11y11) ;
        expected.add(y11x11) ;
        expected.add(x10y1) ;
        for( TestEdge e : expected ) {
            assertTrue( s.contains(e) ) ; }
        
    }
}
