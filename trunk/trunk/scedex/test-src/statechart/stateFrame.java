package statechart;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import display.ExampleLayoutManager;
import display.ExampleSubgraphView;

import sc.model.NodePayloadSC;
import sc.model.NodeSC;
import sc.model.PayloadSC;
import sc.model.SubGraphSC;
import sc.model.WholeGraphSC;
import higraph.higraph.swing.HigraphJComponent;
import higraph.higraph.view.SubgraphView;






public class stateFrame extends JFrame {
	
	
    private Component mydisplay;
	private WholeGraphSC graph = new WholeGraphSC() ;
	private SubGraphSC subgraph = graph.makeSubGraph() ;
    
	private ExampleSubgraphView subgraphView = new ExampleSubgraphView( subgraph, mydisplay) ;
	{ graph.registerView( subgraphView ) ; }
	private ExampleLayoutManager graphLayoutManger = new ExampleLayoutManager(subgraphView) ;
	{ subgraphView.setLayoutManager( graphLayoutManger  ) ; }
	private HigraphJComponent graphDisplay= new HigraphJComponent( subgraphView ) ;
    private int k = 0 ;
	
	stateFrame() {
	    
		add( graphDisplay, BorderLayout.CENTER ) ;
		JToolBar toolBar = new JToolBar() ;
		add( toolBar, BorderLayout.EAST) ;
		
		Action action = new AbstractAction("add root") {
			public void actionPerformed(ActionEvent e) {
				NodeSC n = graph.makeRootNode( new NodePayloadSC(Integer.toString(k++)) ) ;
				subgraph.addTop( n ) ;
                subgraphView.refresh() ;
                graphDisplay.repaint();
			}} ;
		toolBar.add( action ) ;
		
		action = new AbstractAction("delete root") {
			public void actionPerformed(ActionEvent e) {
				List<NodeSC> tops = graph.getTops() ;
				if( tops.size() > 0 ) {
				    NodeSC root = tops.get(1) ;
				    if( root.canDelete() ) {
				        root.delete() ; } }
				subgraphView.refresh() ;
                graphDisplay.repaint();
			}} ;
		toolBar.add( action ) ;
		
		action = new AbstractAction("add nested node") {
			public void actionPerformed(ActionEvent e) {
				List<NodeSC> tops = graph.getTops() ;
                if( tops.size() > 0 ) {
                    NodeSC root =  tops.get(1) ;
                    NodeSC newNode = graph.makeRootNode(  new NodePayloadSC(Integer.toString(k) ) ) ;
                    if( root.canInsertChild(0, newNode) ) {
                        root.insertChild(0, newNode) ; } }
                subgraphView.refresh() ;
                graphDisplay.repaint();
			}} ;
		toolBar.add( action ) ;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ) ;
		setSize(300, 400) ;
	}
}