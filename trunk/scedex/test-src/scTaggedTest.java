import static org.junit.Assert.*;



import org.junit.Before;
import org.junit.Test;

import tagsc.*;

public class scTaggedTest {
	private TaggedNodeSC nodeA;
	private TaggedNodeSC nodeB;
	private TaggedNodeSC nodeC;
	private TaggedNodeSC nodeD;
	private TaggedNodeSC nodeE;
	private TaggedNodeSC nodeF;
	private TaggedNodeSC nodeG;
	private TaggedNodeSC nodeH;
	
	private TaggedWholeGraphSC twholegraph;
	
	private TaggedPayloadSC pnodeA;
	private TaggedPayloadSC pnodeB;
	private TaggedPayloadSC pnodeC;
	private TaggedPayloadSC pnodeD;
	private TaggedPayloadSC pnodeE;
	private TaggedPayloadSC pnodeF;
	private TaggedPayloadSC pnodeG;
	private TaggedPayloadSC pnodeH;
	private TaggedPayloadSC pnodeI;
	private TaggedNodeSC nodeI;
	
	
	@Before
	public void setUp() throws Exception {
		twholegraph = new TaggedWholeGraphSC();
		
		pnodeA = new TaggedPayloadSC("plain state", SCTag.PLAINSTATE);
		nodeA = twholegraph.makeRootNode(pnodeA);
		
		pnodeB = new TaggedPayloadSC("othogonal state", SCTag.ORTHOGONALSTATE);
		nodeB = twholegraph.makeRootNode(pnodeB);
		
		pnodeC = new TaggedPayloadSC("region", SCTag.REGION);
		nodeC = twholegraph.makeRootNode(pnodeC);
		
		pnodeD = new TaggedPayloadSC("forkjoin", SCTag.FORKJOIN);
		nodeD = twholegraph.makeRootNode(pnodeD);
		
		pnodeE = new TaggedPayloadSC("history", SCTag.HISTORY);
		nodeE = twholegraph.makeRootNode(pnodeE);
		
		pnodeF = new TaggedPayloadSC("initial", SCTag.INITIAL);
		nodeF = twholegraph.makeRootNode(pnodeF);
		
		pnodeG = new TaggedPayloadSC("terminate", SCTag.TERMINATE);
		nodeG = twholegraph.makeRootNode(pnodeG);
		
		pnodeH = new TaggedPayloadSC("junction", SCTag.JUNCTION);
		nodeH = twholegraph.makeRootNode(pnodeH);
		
		pnodeI = new TaggedPayloadSC("choice", SCTag.CHOICE);
		nodeI = twholegraph.makeRootNode(pnodeI);
		
		
		}
	@Test
	public void testTag() {
		assertEquals(SCTag.PLAINSTATE,nodeA.getTag());
		assertEquals(SCTag.ORTHOGONALSTATE,nodeB.getTag());
		assertEquals(SCTag.REGION,nodeC.getTag());
		assertEquals(SCTag.FORKJOIN,nodeD.getTag());
		assertEquals(SCTag.HISTORY,nodeE.getTag());
		assertEquals(SCTag.INITIAL,nodeF.getTag());
		assertEquals(SCTag.TERMINATE,nodeG.getTag());
		assertEquals(SCTag.JUNCTION,nodeH.getTag());
		assertEquals(SCTag.CHOICE,nodeI.getTag());
		
		assertEquals(pnodeA.getTag(), SCTag.PLAINSTATE.defaultPayload().getTag()); 
		assertEquals(pnodeB.getTag(), SCTag.ORTHOGONALSTATE.defaultPayload().getTag());
		assertEquals(pnodeC.getTag(), SCTag.REGION.defaultPayload().getTag());
		assertEquals(pnodeD.getTag(), SCTag.FORKJOIN.defaultPayload().getTag());
		assertEquals(pnodeE.getTag(), SCTag.HISTORY.defaultPayload().getTag());
		assertEquals(pnodeF.getTag(), SCTag.INITIAL.defaultPayload().getTag());
		assertEquals(pnodeG.getTag(), SCTag.TERMINATE.defaultPayload().getTag());
		assertEquals(pnodeH.getTag(), SCTag.JUNCTION.defaultPayload().getTag());
		assertEquals(pnodeI.getTag(), SCTag.CHOICE.defaultPayload().getTag());
		}
	@Test
	public void testDefaultTagSeq() {
	    assertEquals(0, SCTag.PLAINSTATE.defaultTagSequence().size());
	    //assertEquals(2, SCTag.ORTHOGONALSTATE.defaultTagSequence().size());
	   
	    assertEquals(0, SCTag.FORKJOIN.defaultTagSequence().size());
	    assertEquals(0, SCTag.HISTORY.defaultTagSequence().size());
	    assertEquals(0, SCTag.INITIAL.defaultTagSequence().size());
	    assertEquals(0, SCTag.REGION.defaultTagSequence().size());
	    assertEquals(0, SCTag.JUNCTION.defaultTagSequence().size());
	    assertEquals(0, SCTag.TERMINATE.defaultTagSequence().size());
	    }
	@Test
	public void testCanInsert() {
	    assertTrue(nodeA.canInsertChild(0,SCTag.PLAINSTATE));
	    assertTrue(nodeA.canInsertChild(0,SCTag.CHOICE));
	    assertTrue(nodeA.canInsertChild(0,SCTag.FORKJOIN));
	    assertTrue(nodeA.canInsertChild(0,SCTag.JUNCTION));
	    assertTrue(nodeA.canInsertChild(0,SCTag.ORTHOGONALSTATE));
	    assertTrue(nodeA.canInsertChild(0,SCTag.REGION));
	    
	    nodeB.insertChild(0, SCTag.REGION);
	    nodeB.insertChild(1, SCTag.REGION);
	    assertFalse(nodeB.canInsertChild(0,SCTag.PLAINSTATE));
	    assertFalse(nodeB.canInsertChild(0,SCTag.CHOICE));
	    assertFalse(nodeB.canInsertChild(0,SCTag.FORKJOIN));
	    assertFalse(nodeB.canInsertChild(0,SCTag.HISTORY));
	    assertFalse(nodeB.canInsertChild(0,SCTag.INITIAL));
	    assertFalse(nodeB.canInsertChild(0,SCTag.TERMINATE));
	    assertFalse(nodeB.canInsertChild(1,SCTag.REGION));
	    
	    }
	@Test
	public void Insert(){
		//initially I have nodeA
		assertTrue(nodeA.getNumberOfChildren()==0);
	   // insert an Choice plainstate[choice]
	   
	   nodeA.insertChild(0, nodeI);
	   assertTrue(nodeA.getNumberOfChildren()==1);
	   assertEquals(SCTag.CHOICE, nodeA.getChild(0).getTag());
	   //plainstate[choice, initial]
	   
	   nodeA.insertChild(1,SCTag.INITIAL);
	   assertTrue(nodeA.getNumberOfChildren()==2);
	   
	   nodeA.insertChild(2, nodeG);
	   assertTrue(nodeA.getNumberOfChildren()==3);
	   
	   assertEquals(SCTag.CHOICE, nodeA.getChild(0).getTag());
	   assertEquals(SCTag.INITIAL, nodeA.getChild(1).getTag());
	   assertEquals(SCTag.TERMINATE, nodeA.getChild(2).getTag());
	   }
	@Test
	public void testCanReplace(){
	   nodeA.insertChild(0, nodeB);
	   nodeA.insertChild(1, nodeD);
	   
	   //nodeA[Othogonal state, forkjoin]
	   assertTrue(nodeA.getNumberOfChildren()==2);
	   assertEquals(nodeB,nodeA.getChild(0));
	   assertEquals(nodeD,nodeA.getChild(1));
	   
	   //nodeA[othogonal state[region,region],forkjoin]
	   nodeB.insertChild(0, SCTag.REGION);
	   nodeB.insertChild(1, SCTag.REGION);
	   assertTrue(nodeB.getNumberOfChildren()==2);
	   assertEquals(nodeB.getTag(),SCTag.ORTHOGONALSTATE);
	   assertFalse(nodeB.canReplace(SCTag.CHOICE));
	   
	   
	   
	   
	   
	   
	}
           

}
