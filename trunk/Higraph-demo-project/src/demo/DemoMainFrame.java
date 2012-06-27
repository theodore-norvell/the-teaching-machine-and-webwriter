package demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;

import tm.backtrack.BTTimeManager;

import higraph.swing.HigraphJComponent;
import higraph.swing.SubgraphMouseAdapter;

import demo.model.*;
import demo.view.DemoHigraphView;
import demo.view.DemoViewFactory;
import demo.view.layout.DemoBoxesInBoxesLayout;
import demo.view.layout.DemoLayoutManager;




public class DemoMainFrame extends JFrame {
	
	private static final long serialVersionUID = -6388967497486007956L;
    
	private BTTimeManager timeMan = new BTTimeManager() ;
	private DemoWholeGraph graph = new DemoWholeGraph(timeMan) ;
    

	// Set up a tree-like display
    private HigraphJComponent graphDisplay0 = new HigraphJComponent( ) ;
    private DemoViewFactory viewFactory0 = new DemoViewFactory(timeMan) ;
    private DemoHigraphView subgraphView0 = viewFactory0.makeHigraphView(graph, graphDisplay0) ;
	{ graphDisplay0.setSubgraphView( subgraphView0 ) ; }
	private DemoLayoutManager graphLayoutManger0 = new DemoLayoutManager() ;
	{ subgraphView0.setLayoutManager( graphLayoutManger0  ) ; }
	
	// Set up an event observer.
	private DemoSGEventObserver observer = new DemoSGEventObserver() ;
	private SubgraphMouseAdapter<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
	                      sgm = new SubgraphMouseAdapter<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge>
	                                 ( subgraphView0, observer ) ;
	{ sgm.installIn( graphDisplay0 ) ; }
    private int k = 0 ;
    
    // Set up a boxes-in-boxes display
    private HigraphJComponent graphDisplay1 = new HigraphJComponent() ;
    private DemoViewFactory viewFactory1 = new DemoViewFactory(timeMan) ;
    private DemoHigraphView subgraphView1 = viewFactory1.makeHigraphView(graph, graphDisplay1) ;
    { graphDisplay1.setSubgraphView( subgraphView1 ) ; }
    private DemoBoxesInBoxesLayout graphLayoutManager1 = new DemoBoxesInBoxesLayout() ;
    { subgraphView1.setLayoutManager( graphLayoutManager1  ) ; }
	
	DemoMainFrame() {
	    JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, graphDisplay0, graphDisplay1 ) ;
	    splitPane.setResizeWeight(0.5) ;
		add( splitPane, BorderLayout.CENTER ) ;
		JToolBar toolBar = new JToolBar() ;
		add( toolBar, BorderLayout.NORTH ) ;
		
		Action action = new AbstractAction("add root") {
			public void actionPerformed(ActionEvent e) {
				graph.makeRootNode( new DemoPayload(Integer.toString(k++), DemoTag.SEQ) ) ;
				graphDisplay0.refresh() ;
				graphDisplay1.refresh() ;
			}} ;
		toolBar.add( action ) ;
		
		action = new AbstractAction("delete root") {
			public void actionPerformed(ActionEvent e) {
				List<DemoNode> tops = graph.getTops() ;
				if( tops.size() > 0 ) {
				    DemoNode root = tops.get(0) ;
				    if( root.canDelete() ) {
				        root.delete() ; } }
                graphDisplay0.refresh();
                graphDisplay1.refresh();
			}} ;
		toolBar.add( action ) ;
		
		action = new AbstractAction("add nested node") {
			public void actionPerformed(ActionEvent e) {
				List<DemoNode> tops = graph.getTops() ;
                if( tops.size() > 0 ) {
                    DemoNode root =  tops.get(0) ;
                    DemoNode newNode = graph.makeRootNode(  new DemoPayload(Integer.toString(k), DemoTag.SEQ ) ) ;
                    if( root.canInsertChild(0, newNode) ) {
                        root.insertChild(0, newNode) ; } }
                graphDisplay0.refresh();
                graphDisplay1.refresh();
			}} ;
		toolBar.add( action ) ;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		setSize(300, 400) ;
	}
	
	public static void main(String[] args ) {
		SwingUtilities.invokeLater( new Runnable() {

			public void run() {
				DemoMainFrame frame = new DemoMainFrame() ;
				frame.setVisible(true) ;
			}} ) ;
	}

}
