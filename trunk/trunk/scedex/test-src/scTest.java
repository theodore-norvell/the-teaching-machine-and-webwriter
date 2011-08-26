import sc.model.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import tm.displayEngine.tmHigraph.EdgeTM;
import tm.displayEngine.tmHigraph.NodePayloadTM;
import tm.displayEngine.tmHigraph.NodeTM;
import tm.displayEngine.tmHigraph.SubgraphTM;
import tm.displayEngine.tmHigraph.WholeGraphTM;
import higraph.model.interfaces.Edge;
import higraph.model.interfaces.Edge.EdgeCategory;

public class scTest {

	
	private NodeSC node;
	private NodeSC nodeR0;
	private NodeSC nodeR0a;
	private NodeSC nodeR0b;
	private NodeSC nodeR0c;
	private NodeSC newNode1;
	private NodeSC newNode2;
	private NodeSC newNode3;
	private NodeSC newNode4;
	private NodePayloadSC payloadnode;
	private NodePayloadSC payloadnodeR0;
	private NodePayloadSC payloadnodeR0a;
	private NodePayloadSC payloadnodeR0b;
	private NodePayloadSC payloadnodeR0c;
	private NodePayloadSC payloadnodeR1;
	
	private SubGraphSC subgraphsc;
	private EdgeSC edgesc;
	private WholeGraphSC wholegraph;
	private NodePayloadSC tempNode1;
	private NodePayloadSC tempNode2;
	private NodePayloadSC tempNode3;
	
	private NodePayloadSC tempNode4;
	@Before
	public void setUp() throws Exception {
		
		payloadnode = new NodePayloadSC("node");
		payloadnodeR0 = new NodePayloadSC("R0");
		payloadnodeR0a = new NodePayloadSC("R0a");
		payloadnodeR0b = new NodePayloadSC("R0b");
		payloadnodeR0c = new NodePayloadSC("R0c");
		payloadnodeR1 = new NodePayloadSC("R1");
		tempNode1 = new NodePayloadSC("tempNode1");
		tempNode2 = new NodePayloadSC("tempNode2");
		tempNode3 = new NodePayloadSC("tempNode3");
		tempNode4 = new NodePayloadSC("tempNode4");
		
		
	    
		wholegraph = new WholeGraphSC();
		node = wholegraph.makeRootNode(payloadnode);
		nodeR0 = wholegraph.makeRootNode(payloadnodeR0);
		nodeR0a = wholegraph.makeRootNode(payloadnodeR0a);
		nodeR0b = wholegraph.makeRootNode(payloadnodeR0b);
		nodeR0c = wholegraph.makeRootNode(payloadnodeR0c);
		newNode1 = wholegraph.makeRootNode(tempNode1);
		newNode2 = wholegraph.makeRootNode(tempNode2);
		newNode3 = wholegraph.makeRootNode(tempNode3);
		newNode4 = wholegraph.makeRootNode(tempNode4);
	  
		node.insertChild(0, nodeR0);
		nodeR0.insertChild(0, nodeR0a);
		nodeR0.insertChild(1, nodeR0b);		
		nodeR0.insertChild(2, nodeR0c);
		subgraphsc = wholegraph.makeSubGraph();
	}
	
	@Test
	public void test0() {
		
		assertTrue(node.getNumberOfChildren() == 1);
		assertEquals(nodeR0,node.getChild(0));
		assertEquals(node, nodeR0.getParent());
		
		assertTrue(nodeR0.getChild(0) == nodeR0a);
		assertTrue(nodeR0.getChild(1)== nodeR0b);
		assertTrue(nodeR0.getChild(2) == nodeR0c);
		assertTrue(nodeR0.getNumberOfChildren() == 3);
		assertEquals(nodeR0, nodeR0a.getParent());
		assertEquals(nodeR0, nodeR0b.getParent());
		assertEquals(nodeR0, nodeR0c.getParent());
		
	}
	
	@Test
	public void testInsertChildR0() {
		
		assertTrue(newNode1.getParent() == null);

	    //now we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c]]
		
		node.insertChild(0, newNode1);
		// node[tempNode1,nodeR0[nodeR0a, nodeR0b, nodeR0c]]
		
		assertTrue(node.getNumberOfChildren() == 2);
		assertTrue(node.getChild(0) == newNode1);
		assertTrue(node.getChild(1) == nodeR0);
		assertTrue(newNode1.getParent() == node);
        assertEquals(node, nodeR0.getParent());
		
        // Now delete it
		newNode1.delete();
		assertTrue(newNode1.deleted());
		// node[nodeR0]

		assertTrue(node.getParent() == null);
		assertTrue(node.getNumberOfChildren() == 1);
		
		
		

		//now insert it to position 1
		node.insertChild(1, newNode2);
		// node[nodeR0,tempNode2]
		assertTrue(node.getNumberOfChildren() == 2);
		assertEquals(nodeR0,node.getChild(0));
		assertEquals(newNode2,node.getChild(1));
		assertTrue(newNode2.getParent() == node);

		// Where can we move it?
		assertTrue(node.canInsertChild(0, newNode2));
		assertTrue(node.canInsertChild(1, newNode2));
		assertTrue(node.canInsertChild(2, newNode2));
		assertFalse(node.canInsertChild(3, newNode2));
        
		
		// Move it to after itself This should actually be no change.
		node.insertChild(2, newNode3);
		// node[nodeR0,tempNode2,tempNode3]  
		//move tempNode2 after tempNode3
		newNode2.detach();
		node.insertChild(2, newNode2);
		
		//now it should be node[nodeR0,tempNode3,tempNode2]
		assertTrue(node.getNumberOfChildren() == 3);
		assertTrue(node.getChild(0) == nodeR0);
		assertTrue(node.getChild(1) == newNode3);
		assertTrue(node.getChild(2)==newNode2);
		assertEquals(nodeR0.getParent() , newNode2.getParent());
        
		// Move it to just before tempNode3
		newNode2.detach();
		node.insertChild(1, newNode2);
		// now it should be node[nodeR0,tempNode2,tempNode3]
		
		assertTrue(node.getNumberOfChildren() == 3);
		assertEquals(nodeR0, node.getChild(0));
		assertEquals(newNode2, node.getChild(1));
		assertEquals(newNode3, node.getChild(2));
       }
	
	@Test
	public void testDetach() {
		//initially we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c]]
		//now detach nodeR0
		
		
		assertEquals(1,node.getNumberOfChildren());
		assertEquals(3,nodeR0.getNumberOfChildren());
		
		assertEquals(nodeR0a, nodeR0.getChild(0));
		assertEquals(nodeR0b, nodeR0.getChild(1));
		assertEquals(nodeR0c, nodeR0.getChild(2));
		assertEquals(node, nodeR0.getParent());
		assertEquals(nodeR0, nodeR0a.getParent());
		assertEquals(nodeR0, nodeR0b.getParent());
		assertEquals(nodeR0, nodeR0c.getParent());
		
		
		nodeR0.detach();
		// the new structure should be node[]
	    assertEquals(0, node.getNumberOfChildren());
        assertEquals(null, nodeR0.getParent());	    
	    assertEquals(nodeR0a, nodeR0.getChild(0));
		assertEquals(nodeR0b, nodeR0.getChild(1));
		assertEquals(nodeR0c, nodeR0.getChild(2));
		
		assertEquals(nodeR0, nodeR0a.getParent());
		assertEquals(nodeR0, nodeR0b.getParent());
		assertEquals(nodeR0, nodeR0c.getParent());
		
        // now insert tempNode1 to node
		node.insertChild(0, newNode1);
		// the structure now should be node[tempNode1]
		assertEquals(1,node.getNumberOfChildren());
		assertEquals(newNode1,node.getChild(0));
		//now we will insert nodeR0 and its decendents
		newNode1.insertChild(0, nodeR0);
		// the structure should be node[tempNode1[nodeR0[nodeR0a,nodeROb,nodeR0c]]]
		assertEquals(1,newNode1.getNumberOfChildren());
		assertEquals(3,nodeR0.getNumberOfChildren());
		assertEquals(nodeR0,newNode1.getChild(0));
		assertEquals(nodeR0a, nodeR0.getChild(0));
		assertEquals(nodeR0b, nodeR0.getChild(1));
		assertEquals(nodeR0c, nodeR0.getChild(2));
		}
	@Test
	public void testPermute() {
		//initially we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c]}
		int [] p = {1,0,2};
		nodeR0.permuteChildren(p);
		// we should be able to get node[nodeR0[nodeR0b,nodeR0a,nodeR0c]]
		assertEquals(nodeR0, nodeR0a.getParent());
		assertEquals(nodeR0, nodeR0b.getParent());
		assertEquals(nodeR0, nodeR0c.getParent());
		assertEquals(node, nodeR0.getParent());
		
		
		assertEquals(nodeR0b,nodeR0.getChild(0));
		assertEquals(nodeR0a,nodeR0.getChild(1));
		assertEquals(nodeR0c,nodeR0.getChild(2));
		
		nodeR0.permuteChildren(p);
		// after this permutation it should be node[nodeR0[nodeR0a, nodeR0b, nodeR0c]]
		assertEquals(nodeR0, nodeR0a.getParent());
		assertEquals(nodeR0, nodeR0b.getParent());
		assertEquals(nodeR0, nodeR0c.getParent());
		assertEquals(node, nodeR0.getParent());
		
		assertEquals(nodeR0a,nodeR0.getChild(0));
		assertEquals(nodeR0b,nodeR0.getChild(1));
		assertEquals(nodeR0c,nodeR0.getChild(2));
		}
	@Test
	public void testCanReplace() {
		//initially we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c]}
		//nodeR0a.duplicate(); // how to use this duplicated node?? // yeli
		
		assertFalse(nodeR0a.canReplace(nodeR0a)); // can not replace by itself 
		assertFalse(nodeR0a.canReplace(nodeR0));  // can not replace by its ancestor
		assertTrue(nodeR0a.canReplace(newNode1));
		assertTrue(nodeR0a.canReplace(newNode2));
		assertTrue(nodeR0a.canReplace(newNode3));
		
		
		}
	@Test
	public void testReplace() {
		//initially we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c]}
		// now we want to replace nodeR0a by tempNode1
		assertTrue(newNode1.getParent()==null);
		//nodeR0a.detach();
		nodeR0a.replace(newNode1);
		// After replacement it should be node[nodeR0[tempNode1, nodeR0b, nodeR0c]}
		assertTrue(nodeR0.getNumberOfChildren() == 3);
		assertTrue(nodeR0.getChild(0) == newNode1);
		assertTrue(nodeR0.getChild(1) == nodeR0b);
		assertTrue(nodeR0.getChild(2) == nodeR0c);
		
		
        assertEquals(nodeR0, newNode1.getParent());
		
		}
	
	@Test
	public void testSubgraph() {
	    //initially the subgraph is empty.
		assertEquals(0, subgraphsc.getNodes().size());
		subgraphsc.addTop(nodeR0);
		//now nodeR0 is in the subgraph.
		
		assertEquals(wholegraph, subgraphsc.getWholeGraph());
		}
	@Test
	public void testEdge(){
		//initially we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c]}
		node.insertChild(1, newNode1);
		//then we have node[nodeR0[nodeR0a, nodeR0b, nodeR0c], tempNode1]
	    newNode1.insertChild(0, newNode2);
	    newNode1.insertChild(1, newNode3);
	    newNode2.insertChild(0, newNode4);
	    // node[nodeR0[nodeR0a, nodeR0b, nodeR0c], tempNode1[tempNode2[tempNode4],tempNode3]]
	    // start making edges
	    
	    EdgePayloadSC edgeP1 = new EdgePayloadSC("Inedge1");
	    EdgePayloadSC edgeP2 = new EdgePayloadSC("Inedge2");
	    EdgePayloadSC edgeP3 = new EdgePayloadSC("Outedge1");
	    EdgePayloadSC edgeP4 = new EdgePayloadSC("Upedge");
	    EdgePayloadSC edgeP5 = new EdgePayloadSC("downedge");
	    EdgePayloadSC edgeP6 = new EdgePayloadSC("deepInedge");
	    EdgePayloadSC edgeP7 = new EdgePayloadSC("deepOutedge");
	    EdgePayloadSC edgeP8 = new EdgePayloadSC("internal");
	    
	    EdgeSC inEdge1 = wholegraph.makeEdge(nodeR0a, newNode1, edgeP1);
	    EdgeSC inEdge2 = wholegraph.makeEdge(node, newNode1, edgeP2);
	    EdgeSC outEdge1 = wholegraph.makeEdge(newNode1, node, edgeP3);
	    EdgeSC upEdge = wholegraph.makeEdge(newNode2, newNode1, edgeP4);
	    EdgeSC downEdge1 = wholegraph.makeEdge(newNode1, newNode4, edgeP5);
	    EdgeSC deepInEdge = wholegraph.makeEdge(nodeR0a, newNode4, edgeP6);
	    EdgeSC deepOutEdge = wholegraph.makeEdge(newNode4, nodeR0b, edgeP7);
	    EdgeSC internalEdge = wholegraph.makeEdge(newNode4, newNode3, edgeP8);
	    
	    assertEquals(EdgeCategory.IN,newNode1.categorize(inEdge2));
	    //assertEquals(EdgeCategory.IN,newNode1.categorize(upEdge)); // should be in? // up/in?
	    assertEquals(EdgeCategory.IN,newNode1.categorize(inEdge1));
	    assertEquals(EdgeCategory.OUT,nodeR0a.categorize(inEdge1));
	    assertEquals(EdgeCategory.OUT,nodeR0a.categorize(deepInEdge));
	    //assertEquals(EdgeCategory.DEEP_IN,newNode4.categorize(deepInEdge)); //
	    
	    assertEquals(1, node.enteringEdges().size());
	    assertEquals(1, node.exitingEdges().size());
	    assertEquals(3, newNode1.enteringEdges().size());
	    assertEquals(2, newNode4.enteringEdges().size());
	    
	    assertTrue(newNode1.getParent()==node);
	    // detach the tempNode1
	    newNode1.detach();
	    //node[nodeR0[nodeR0a, nodeR0b, nodeR0c]]     tempNode1[tempNode2[tempNode4],tempNode3]
	    assertTrue(newNode1.getParent()==null);
	    assertEquals(node,inEdge2.getSource());
	    assertEquals(newNode1,inEdge2.getTarget());
	    assertEquals(nodeR0a,inEdge1.getSource());
	    assertEquals(newNode1,inEdge1.getTarget());
	    
	    //Now we want to change the source and target of inEdge1
	    assertEquals(nodeR0a,inEdge1.getSource());
	    assertEquals(newNode1,inEdge1.getTarget());
	    //after changes (nodeR0c,node);
	    inEdge1.changeSource(nodeR0c);
	    inEdge1.changeTarget(node);
	    assertEquals(nodeR0c,inEdge1.getSource());
	    assertEquals(node,inEdge1.getTarget());
	    
	    
	    
	    
	    
		
	}
}

